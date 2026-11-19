package com.placify.auth;

import com.placify.exception.BadRequestException;
import com.placify.exception.EmailDeliveryException;
import com.placify.user.User;
import com.placify.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private static final int TOKEN_BYTES = 32;
    private static final int TOKEN_LIFETIME_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Value("${app.mail.from:}")
    private String fromAddress;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Transactional
    public void requestPasswordReset(String email) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (!StringUtils.hasText(mailHost) || mailSender == null || !StringUtils.hasText(fromAddress)) {
            throw new EmailDeliveryException("Password reset email delivery is not configured.");
        }

        userRepository.findByEmail(email.trim()).ifPresent(user -> sendResetEmail(user, mailSender));
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        PasswordResetToken resetToken = tokenRepository
                .findByTokenHashAndExpiresAtAfter(hashToken(rawToken), LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException("The password reset link is invalid or has expired."));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.deleteByUserId(user.getId());
    }

    private void sendResetEmail(User user, JavaMailSender mailSender) {
        byte[] tokenBytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(tokenBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        tokenRepository.deleteByUserId(user.getId());
        tokenRepository.save(PasswordResetToken.builder()
                .user(user)
                .tokenHash(hashToken(rawToken))
                .expiresAt(LocalDateTime.now().plusMinutes(TOKEN_LIFETIME_MINUTES))
                .build());

        String resetUrl = frontendUrl.replaceAll("/+$", "") + "/reset-password?token=" + rawToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(user.getEmail());
        message.setSubject("Reset your Placify password");
        message.setText("We received a request to reset your Placify password.\n\n"
                + "Use this link within " + TOKEN_LIFETIME_MINUTES + " minutes to choose a new password:\n"
                + resetUrl + "\n\n"
                + "If you did not request this, you can ignore this email.");
        try {
            mailSender.send(message);
        } catch (MailException exception) {
            tokenRepository.deleteByUserId(user.getId());
            log.error("Password reset email delivery failed", exception);
        }
    }

    private String hashToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}

package com.placify.auth;

import com.placify.exception.BadRequestException;
import com.placify.user.User;
import com.placify.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PasswordResetServiceTest {

    private UserRepository userRepository;
    private PasswordResetTokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;
    private PasswordResetService service;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        userRepository = mock(UserRepository.class);
        tokenRepository = mock(PasswordResetTokenRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        ObjectProvider<JavaMailSender> mailSenderProvider = mock(ObjectProvider.class);
        service = new PasswordResetService(userRepository, tokenRepository, passwordEncoder, mailSenderProvider);
    }

    @Test
    void resetPasswordUpdatesPasswordAndInvalidatesUserTokens() {
        User user = User.builder().id(42L).password("old-hash").build();
        PasswordResetToken token = PasswordResetToken.builder().user(user).build();
        when(tokenRepository.findByTokenHashAndExpiresAtAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(token));
        when(passwordEncoder.encode("new-password")).thenReturn("new-hash");

        service.resetPassword("valid-reset-token", "new-password");

        org.junit.jupiter.api.Assertions.assertEquals("new-hash", user.getPassword());
        verify(userRepository).save(user);
        verify(tokenRepository).deleteByUserId(42L);
    }

    @Test
    void resetPasswordRejectsMissingOrExpiredToken() {
        when(tokenRepository.findByTokenHashAndExpiresAtAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> service.resetPassword("expired-token", "new-password"));

        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).deleteByUserId(anyLong());
    }
}

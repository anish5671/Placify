package com.placify.auth;

import com.placify.exception.BadRequestException;
import com.placify.security.JwtTokenProvider;
import com.placify.user.User;
import com.placify.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordResetService passwordResetService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .targetCompany(request.getTargetCompany())
                .targetRole(request.getTargetRole())
                .graduationYear(request.getGraduationYear())
                .role("ROLE_USER")
                .build();

        User savedUser = userRepository.save(user);

        String jwt = tokenProvider.generateTokenFromUsername(savedUser.getUsername(), savedUser.getId());

        return AuthResponse.builder()
                .token(jwt)
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .targetCompany(savedUser.getTargetCompany())
                .targetRole(savedUser.getTargetRole())
                .graduationYear(savedUser.getGraduationYear())
                .role(savedUser.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return AuthResponse.builder()
                .token(jwt)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .targetCompany(user.getTargetCompany())
                .targetRole(user.getTargetRole())
                .graduationYear(user.getGraduationYear())
                .role(user.getRole())
                .build();
    }

    public void requestPasswordReset(String email) {
        passwordResetService.requestPasswordReset(email);
    }

    public void resetPassword(String token, String password) {
        passwordResetService.resetPassword(token, password);
    }
}

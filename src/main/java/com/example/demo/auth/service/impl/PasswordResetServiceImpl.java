package com.example.demo.auth.service.impl;

import com.example.demo.auth.entity.PasswordResetToken;
import com.example.demo.auth.repository.PasswordResetTokenRepository;
import com.example.demo.auth.service.PasswordResetService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetServiceImpl(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PasswordResetToken createToken(User user) {

        byte[] random = new byte[64];

        new SecureRandom().nextBytes(random);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(random);

        PasswordResetToken resetToken = new PasswordResetToken(
                token,
                user,
                Instant.now().plus(15, java.time.temporal.ChronoUnit.MINUTES)
        );

        return passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new BadRequestException("Invalid reset token")
                        );

        if (resetToken.isExpired()) {
            throw new BadRequestException("Reset token expired");
        }

        if (resetToken.isUsed()) {
            throw new BadRequestException("Reset token already used");
        }

        User user = resetToken.getUser();

        user.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        resetToken.markUsed();

        passwordResetTokenRepository.save(resetToken);
    }
}
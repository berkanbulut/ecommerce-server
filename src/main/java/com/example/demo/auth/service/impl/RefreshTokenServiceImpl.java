package com.example.demo.auth.service.impl;

import com.example.demo.auth.entity.RefreshToken;
import com.example.demo.auth.repository.RefreshTokenRepository;
import com.example.demo.auth.service.RefreshTokenService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshDays;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${app.security.jwt.refresh-days}") long refreshDays
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshDays = refreshDays;
    }

    @Override
    public RefreshToken create(User user) {

        byte [] randomBytes = new byte[64];

        secureRandom.nextBytes(randomBytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken(
                token,
                user,
                Instant.now().plus(refreshDays, ChronoUnit.DAYS)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verify(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new BadRequestException("Invalid refresh token")
                );

        if (refreshToken.isRevoked()) {
            throw new BadRequestException("Refresh token revoked");
        }

        if (refreshToken.isExpired()) {
            throw new BadRequestException("Refresh token expired");
        }

        return refreshToken;
    }

    @Override
    public void revoke(RefreshToken refreshToken) {
        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken rotate(RefreshToken oldToken) {

        oldToken.revoke();

        refreshTokenRepository.save(oldToken);

        return create(oldToken.getUser());
    }

    @Override
    public void logoutCurrent(String refreshToken) {
        refreshTokenRepository.revokeByToken(refreshToken);
    }

    @Override
    public void logoutAll(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }
}
package com.example.demo.auth.service;

import com.example.demo.auth.entity.RefreshToken;
import com.example.demo.user.entity.User;

public interface RefreshTokenService {
    RefreshToken create(User user);
    RefreshToken verify(String token);
    void revoke(RefreshToken refreshToken);
    RefreshToken rotate(RefreshToken oldToken);
    void logoutCurrent(String refreshToken);
    void logoutAll(Long userId);

}

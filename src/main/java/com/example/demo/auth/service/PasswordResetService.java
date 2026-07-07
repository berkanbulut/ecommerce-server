package com.example.demo.auth.service;

import com.example.demo.auth.entity.PasswordResetToken;
import com.example.demo.user.entity.User;

public interface PasswordResetService {
    PasswordResetToken createToken(User user);
    void resetPassword(String token, String newPassword);
}

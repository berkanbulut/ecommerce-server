package com.example.demo.auth.controller;

import com.example.demo.auth.dto.*;
import com.example.demo.auth.entity.PasswordResetToken;
import com.example.demo.auth.entity.RefreshToken;
import com.example.demo.auth.service.PasswordResetService;
import com.example.demo.auth.service.RefreshTokenService;
import com.example.demo.auth.service.impl.AuthServiceImpl;
import com.example.demo.config.CookieProperties;
import com.example.demo.exception.NotFoundException;
import com.example.demo.response.ApiResponse;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordResetService passwordResetService;
    private final CookieProperties cookieProperties;

    public AuthController(
            AuthServiceImpl authService,
            RefreshTokenService refreshTokenService,
            CustomUserDetailsService customUserDetailsService,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordResetService passwordResetService,
            CookieProperties cookieProperties
    ) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
        this.cookieProperties = cookieProperties;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "User registered successfully",
                                authService.register(registerRequest)
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {

        AuthResult result = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite(cookieProperties.getSameSite())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LoginResponse loginResponse = new LoginResponse(
                result.accessToken(),
                "Bearer"
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        loginResponse
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {

        RefreshToken oldToken = refreshTokenService.verify(refreshToken);

        RefreshToken newToken = refreshTokenService.rotate(oldToken);

        UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(newToken.getUser().getUsername());

        String accessToken = jwtService.generateAccessToken(userDetails);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newToken.getToken())
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/api/v1/auth")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite(cookieProperties.getSameSite())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LoginResponse loginResponse = new LoginResponse(
                accessToken,
                "Bearer"
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Token refreshed successfully",
                        loginResponse
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {

        refreshTokenService.logoutCurrent(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/api/v1/auth")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(
                ApiResponse.success("Logout successful")
        );
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
            Authentication authentication
    ) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        NotFoundException.of(
                                "User",
                                "username",
                                username
                        )
                );

        refreshTokenService.logoutAll(user.getId());

        return ResponseEntity.ok(
                ApiResponse.success("Logged out from all devices successfully")
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgot(
            @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) {

        userRepository.findByEmail(forgotPasswordRequest.email())
                .ifPresent(user -> {
                    PasswordResetToken token = passwordResetService.createToken(user);

                    System.out.println(
                            "Password reset token for user: " + token.getToken()
                    );
                });

        return ResponseEntity.ok(
                ApiResponse.success(
                        "If the email exists, a reset link has been sent"
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> reset(
            @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {

        passwordResetService.resetPassword(
                resetPasswordRequest.token(),
                resetPasswordRequest.newPassword()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Password reset successful")
        );
    }
}
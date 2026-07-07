package com.example.demo.auth.repository;

import com.example.demo.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("update RefreshToken rt set rt.revoked = true where rt.token = :token")
    int revokeByToken(String token);

    @Transactional
    @Modifying
    @Query("update RefreshToken rt set rt.revoked = true where rt.user.id = :userId")
    int revokeAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken rt where rt.user.id = :userId")
    int deleteAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken rt where rt.expiresAt < :now or rt.revoked = true")
    int cleanup(Instant now);
}
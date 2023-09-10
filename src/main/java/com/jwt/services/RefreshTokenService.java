package com.jwt.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jwt.entities.RefreshToken;
import com.jwt.entities.User;
import com.jwt.repositories.RefreshTokenRepository;

/**
 * This service class is responsible for managing refresh tokens, including creation and verification.
 * Refresh tokens are used to obtain new access tokens after the expiration of the original access token.
 */
@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    @Value("${refresh.token.validity}")
    private long refreshTokenValidity;// Refresh token validity period in milliseconds

    /**
     * Create or update a refresh token for a given username.
     *
     * @param username The username for which the refresh token is created or updated.
     * @return The created or updated RefreshToken.
     */
    public RefreshToken createRefreshToken(String username) {
        User user = userService.getUserByUsername(username);
        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .user(userService.getUserByUsername(username))
                    .build();
        } else {
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        user.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    /**
     * Verify the validity of a refresh token.
     *
     * @param refreshToken The refresh token to verify.
     * @return The verified RefreshToken.
     * @throws RuntimeException if the refresh token is invalid or expired.
     */
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken tempRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Given Token Doesn't Exist"));

        if (tempRefreshToken.getExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(tempRefreshToken);
            throw new RuntimeException("RefreshToken Expired");
        } else {
            return tempRefreshToken;
        }
    }
}

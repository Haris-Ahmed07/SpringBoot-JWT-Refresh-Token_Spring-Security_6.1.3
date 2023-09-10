package com.jwt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.entities.RefreshToken;

/**
 * This interface defines a repository for managing RefreshToken entities.
 * It extends JpaRepository for basic CRUD operations on RefreshToken entities.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    /**
     * Find a RefreshToken entity by its refresh token string.
     *
     * @param token The refresh token string to search for.
     * @return An optional containing the RefreshToken if found, or empty if not found.
     */
    Optional<RefreshToken> findByRefreshToken(String token);
}

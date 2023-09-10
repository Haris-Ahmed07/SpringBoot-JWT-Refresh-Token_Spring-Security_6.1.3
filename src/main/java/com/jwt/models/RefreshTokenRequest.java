package com.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to represent an incoming HTTP request for refreshing a JWT token.
 * It is used to capture the refresh token from the request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RefreshTokenRequest {

    // The refresh token provided in the request
    private String refreshToken;
}

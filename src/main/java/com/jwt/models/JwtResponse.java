package com.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to represent an HTTP response containing JWT-related information.
 * It is used to build and send an HTTP response to the user, including the JWT token,
 * refresh token, and userName.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JwtResponse {

    // The JWT token to be sent in the response
    private String jwtToken;

    // The refresh token to be sent in the response
    private String refreshToken;

    // The userName associated with the response
    private String username;
}

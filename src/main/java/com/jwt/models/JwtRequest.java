package com.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to represent an incoming HTTP request for JWT authentication.
 * It is used to capture the userName and password from the request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JwtRequest {

    // The userName provided in the request
    String username;

    // The password provided in the request
    String password;
}

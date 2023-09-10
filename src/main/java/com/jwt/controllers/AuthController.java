package com.jwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.entities.RefreshToken;
import com.jwt.entities.User;
import com.jwt.helper.JwtUtil;
import com.jwt.models.JwtRequest;
import com.jwt.models.JwtResponse;
import com.jwt.models.RefreshTokenRequest;
import com.jwt.services.RefreshTokenService;
import com.jwt.services.UserService;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil helper;

    // EndPoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        // Authenticate the user
        doAuthenticate(jwtRequest);

        // Fetch the user details
        User user = userService.getUserByUsername(jwtRequest.getUsername());

        // Generate a JWT token
        String token = this.helper.generateToken(user);

        // Create a refresh token for the user
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        // Build the response containing JWT token and refresh token
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .username(user.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Helper method for user authentication
    private void doAuthenticate(JwtRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());

        try {
            manager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password!!");
        }
    }

    // EndPoint for refreshing the JWT token using a refresh token
    @PostMapping("/refresh")
    public JwtResponse refreshJwtToken(@RequestBody RefreshTokenRequest request) {
        // Verify the refresh token
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        // Get the user associated with the refresh token
        User user = refreshToken.getUser();

        // Generate a new JWT token
        String token = helper.generateToken(user);

        // Build the response containing the new JWT token and refresh token
        return JwtResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .jwtToken(token)
                .username(user.getUsername()).build();
    }

    // Exception handler for BadCredentialsException
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid!!";
    }
}

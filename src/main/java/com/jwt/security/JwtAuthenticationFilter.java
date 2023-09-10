package com.jwt.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.helper.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class is responsible for filtering incoming HTTP requests, extracting JWT tokens,
 * and authenticating users based on the tokens.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the header (Bearer token) from the HTTP Request
        String requestHeader = request.getHeader("Authorization");
        
        // Initialize variables for username and token
        String username = null;
        String token = null;

        // Check if the header is not null and starts with "Bearer"
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            // Extract the token from the header
            token = requestHeader.substring(7);
           
            try {
                // Extract the userName from the token
                username = this.jwtUtil.getUsernameFromToken(token);
                
            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username!!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given JWT token is expired!!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changes have been made in the token!! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // If the header is null or doesn't start with "Bearer", log an error
            logger.info("Invalid Header Value!!");
        }

        /*
         * If the header is not null and the security context doesn't already contain an authentication object,
         * proceed with authentication.
         */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Fetch user details object based on the userName
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the token for expiration and other issues
            Boolean validateToken = this.jwtUtil.validateToken(token, userDetails);

            // If the token is valid, proceed with authentication
            if (validateToken) {
                /*
                 * Create a new authentication token containing user details, no password (since it's not stored), and authorities.
                 * This token represents the authenticated user.
                 */
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                /*
                 * Set additional details about the authentication request, such as the remote IP address and session ID,
                 * on the Authentication object. This information can be useful for auditing and logging purposes.
                 */
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                /*
                 * Set the current authentication in the security context for the current thread of execution.
                 * The security context holds information about the current authentication and, optionally, the current authorization.
                 */
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // If the token is not valid, log an error
                logger.info("Validation fails!!");
            }
        }

        /*
         * Call filterChain.doFilter(request, response) at the end of a filter's doFilterInternal method to ensure
         * that the request is properly processed by all filters in the chain before reaching its target resource.
         */
        filterChain.doFilter(request, response);
    }
}

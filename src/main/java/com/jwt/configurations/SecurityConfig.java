package com.jwt.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.security.JwtAuthenticationEntryPoint;
import com.jwt.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

	
	// Inject the JwtAuthenticationEntryPoint and JwtAuthenticationFilter beans
    @Autowired
    private JwtAuthenticationEntryPoint point;
	
	@Autowired
	private JwtAuthenticationFilter filter;
	
	// Define a bean for the SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection
            .cors(cors -> cors.disable()) // Disable CORS protection
            .authorizeHttpRequests(auth -> auth // Configure authorization rules
                .requestMatchers(HttpMethod.GET, "/getall").permitAll() // Permit all GET requests to "/getall"
                .requestMatchers(HttpMethod.POST, "/login", "/refresh","/add-user","/add-users").permitAll() // Permit all POST requests to "/login", "/refresh", "/add-user", and "/add-users"
                .requestMatchers(HttpMethod.GET, "/user").hasRole("USER") // Only allow users with the "USER" role to access GET requests to "/user"
                .requestMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN") // Only allow users with the "ADMIN" role to access GET requests to "/admin"
                .anyRequest().authenticated() // Require authentication for all other requests
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(point)) // Set the authentication entry point
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Set the session creation policy

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); // Add the JwtAuthenticationFilter before the UsernamePasswordAuthenticationFilter

        return http.build();
    }
	
    // Define a bean for the AuthenticationManager
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception
	{
		return builder.getAuthenticationManager();
	}
	
}

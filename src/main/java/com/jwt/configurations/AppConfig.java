package com.jwt.configurations;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jwt.helper.CustomUserDetailsService;



@Configuration
public class AppConfig {

	 // Define a bean for the UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        // Use our custom implementation of UserDetailsService
        return new CustomUserDetailsService();
    }

    // Define a bean for the password encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Use the BCrypt password encoder
        return new BCryptPasswordEncoder();
    }

	
    // Define a bean for the authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        // Set the UserDetailsService and password encoder for the authentication provider
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }	
	
}

package com.jwt.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jwt.entities.User;
import com.jwt.services.UserService;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	// Load user details by userName for authentication and authorization
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Fetch the user by userName
		User user = userService.getUserByUsername(username);

		// If the user is not found, throw an exception
		if (user == null) {
			throw new UsernameNotFoundException("User Not Found");
		}

		// Create a custom user details object for Spring Security
		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		return customUserDetails;
	}
}

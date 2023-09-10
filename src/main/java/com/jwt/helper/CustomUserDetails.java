package com.jwt.helper;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jwt.entities.User;

@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails {
	
	@Autowired
	private User user;
	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	// Define the authorities (roles) for the user
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
		return List.of(simpleGrantedAuthority);
	}

	// Get the user's password
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	// Get the user's username
	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// Indicate if the user's account is not expired
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// Indicate if the user's account is not locked
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// Indicate if the user's credentials are not expired
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Indicate if the user is enabled
	@Override
	public boolean isEnabled() {
		return true;
	}
}

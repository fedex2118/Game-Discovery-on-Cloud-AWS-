package com.games.games_api.utils;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class DevelopmentUtils {

	public static UserDetails developmentUserDetails(String username, String role) {
        
        List<SimpleGrantedAuthority> authorities = List.of(
        		new SimpleGrantedAuthority(role));
        
        return new User(username, "", authorities);
	}
}

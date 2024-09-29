package com.users.user_preferences_api.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.users.user_preferences_api.application.AppProperties;
import com.users.user_preferences_api.utils.DevelopmentUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AppProperties appProperties;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		
		final UserDetails userDetails;
		
		// if on dev environment
		if(appProperties.getDevConfig().isDevelopment()) {
			userDetails = DevelopmentUtils.developmentUserDetails(appProperties.getDevConfig().getUsername(),
					appProperties.getDevConfig().getRole());
			initUsernamePasswordAuthenticationToken(request, userDetails);
			filterChain.doFilter(request, response);
			return;
		}
		
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userDetails = jwtService.parseToken(jwt);

		initUsernamePasswordAuthenticationToken(request, userDetails);
		
		filterChain.doFilter(request, response);
	}
	
	private void initUsernamePasswordAuthenticationToken(HttpServletRequest request, UserDetails userDetails) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}

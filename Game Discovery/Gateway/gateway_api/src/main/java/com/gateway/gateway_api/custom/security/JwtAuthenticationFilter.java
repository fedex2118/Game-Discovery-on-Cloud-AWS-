package com.gateway.gateway_api.custom.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gateway.gateway_api.application.AppProperties;
import com.gateway.gateway_api.custom.utils.DevelopmentUtils;
import com.gateway.gateway_api.custom.utils.WebConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(2)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AppProperties appProperties;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		
		// ignore auth path
		if (request.getServletPath().contains(WebConstants.AUTH)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final UserDetails userDetails;
		
		// if on dev environment
		if(appProperties.getDevConfig().isDevelopment()) {
			userDetails = DevelopmentUtils.developmentUserDetails(appProperties.getDevConfig().getUsername(),
					appProperties.getDevConfig().getRole());
			initUsernamePasswordAuthenticationToken(request, userDetails, null);
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

		initUsernamePasswordAuthenticationToken(request, userDetails, jwt);
		
		filterChain.doFilter(request, response);
	}
	
	private void initUsernamePasswordAuthenticationToken(HttpServletRequest request, UserDetails userDetails,
			String credentials) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
				credentials,
				userDetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}

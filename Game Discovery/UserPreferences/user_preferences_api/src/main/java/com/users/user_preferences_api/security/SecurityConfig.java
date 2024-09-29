package com.users.user_preferences_api.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	// here we specify which path doesn't require authentication
	private static final String[] WHITE_LIST_URL = { 
			"/v2/api-docs", 
			"/v3/api-docs", 
			"/v3/api-docs/**",
			"/swagger-resources", 
			"/swagger-resources/**", 
			"/configuration/ui", 
			"/configuration/security",
			"/swagger-ui/**", 
			"/webjars/**", 
			"/swagger-ui.html", 
			"/error" };

	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						req -> req.requestMatchers(WHITE_LIST_URL)
						.permitAll()
						.anyRequest()
						.authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}

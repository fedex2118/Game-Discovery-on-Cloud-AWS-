package com.users.users_api.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.users.users_api.config.JwtConfig;

@Configuration
@ConfigurationProperties(prefix="application")
public class AppProperties {

	private JwtConfig jwtConfig = new JwtConfig();

	public JwtConfig getJwtConfig() {
		return jwtConfig;
	}

	public void setJwtConfig(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
}

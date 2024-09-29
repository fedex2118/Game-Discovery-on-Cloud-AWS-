package com.users.user_preferences_api.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.users.user_preferences_api.config.DevConfig;
import com.users.user_preferences_api.config.JwtConfig;

@Configuration
@ConfigurationProperties(prefix="application")
public class AppProperties {

	private DevConfig devConfig = new DevConfig();
	private JwtConfig jwtConfig = new JwtConfig();

	public DevConfig getDevConfig() {
		return devConfig;
	}

	public void setDevConfig(DevConfig devConfig) {
		this.devConfig = devConfig;
	}

	public JwtConfig getJwtConfig() {
		return jwtConfig;
	}

	public void setJwtConfig(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
	
}

package com.gateway.gateway_api.config;

public class DevConfig {

	private boolean development = false;
	
	private String username;
	
	private String role;

	public boolean isDevelopment() {
		return development;
	}

	public void setDevelopment(boolean development) {
		this.development = development;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}

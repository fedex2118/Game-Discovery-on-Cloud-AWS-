package com.gateway.gateway_api.users.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserSignUpPost {

	// users service
	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String username;
	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String password;
	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String email;
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}

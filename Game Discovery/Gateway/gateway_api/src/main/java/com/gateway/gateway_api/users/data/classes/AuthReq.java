package com.gateway.gateway_api.users.data.classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthReq {

	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String email;
	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

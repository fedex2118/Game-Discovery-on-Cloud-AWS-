package com.gateway.gateway_api.users.data.classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserReqPatch {

	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

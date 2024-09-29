package com.games.games_api.platforms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlatformReqPost {

	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

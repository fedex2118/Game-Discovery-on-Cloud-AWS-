package com.games.games_api.publishers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PublisherReqPatch {

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

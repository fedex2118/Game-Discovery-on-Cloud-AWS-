package com.games.games_api.games.dto;

import jakarta.validation.constraints.NotNull;

public class GameReqPatchStatus {

	@NotNull(message="Field is mandatory")
	private Long id;
	
	@NotNull(message="Field is mandatory")
	private int status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

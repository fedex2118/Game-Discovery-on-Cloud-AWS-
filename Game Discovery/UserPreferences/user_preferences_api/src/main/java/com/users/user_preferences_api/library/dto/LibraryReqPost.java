package com.users.user_preferences_api.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LibraryReqPost {

	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long userId;
	
	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long gameId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

}

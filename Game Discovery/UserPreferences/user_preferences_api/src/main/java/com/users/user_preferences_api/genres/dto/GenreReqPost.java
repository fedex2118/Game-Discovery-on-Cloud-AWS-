package com.users.user_preferences_api.genres.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GenreReqPost {

	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long userId;
	
	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long genreId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}
	
	
}

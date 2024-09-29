package com.users.user_preferences_api.genres.dto;

public class GenreResp {

	private Long userId;
	
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

package com.users.user_preferences_api.library.dto;

public class LibraryResp {

	private Long userId;
	
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

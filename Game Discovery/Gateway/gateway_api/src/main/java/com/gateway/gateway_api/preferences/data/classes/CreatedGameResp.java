package com.gateway.gateway_api.preferences.data.classes;

public class CreatedGameResp {

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

package com.gateway.gateway_api.games.data.classes;

public class GameStatusResp {

	private Long id;
	
	private GameStatus status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}
}

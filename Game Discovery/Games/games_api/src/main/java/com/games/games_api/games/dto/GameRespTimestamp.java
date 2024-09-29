package com.games.games_api.games.dto;

import java.time.Instant;

public class GameRespTimestamp extends GameResp {

	private Instant timestamp;
	
	private GameStatus status;
	
	public GameRespTimestamp() {
		super();
	}
	
	public GameRespTimestamp(Instant timestamp) {
		super();
		this.timestamp = timestamp;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	
}

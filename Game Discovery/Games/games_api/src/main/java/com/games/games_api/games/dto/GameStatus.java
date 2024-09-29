package com.games.games_api.games.dto;

public enum GameStatus {

	PENDING(0),
    APPROVED(1),
    REJECTED(2);

    private final int code;

    GameStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

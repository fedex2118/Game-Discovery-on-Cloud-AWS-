package com.gateway.gateway_api.games.data.classes;

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

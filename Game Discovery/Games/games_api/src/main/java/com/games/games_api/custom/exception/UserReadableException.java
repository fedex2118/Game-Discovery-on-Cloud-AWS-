package com.games.games_api.custom.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@SuppressWarnings("serial")
@Schema
public class UserReadableException extends RuntimeException {

	private String code = "400";
	
    public UserReadableException(String message) {
        super(message);
    }
    
    public UserReadableException(String message, String code) {
        super(message);
        this.code = code;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
}


package com.gateway.gateway_api.custom.exception;

import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gateway.gateway_api.custom.response.HttpStatusDeserializer;

public class ApiError {

	@JsonDeserialize(using = HttpStatusDeserializer.class)
	private HttpStatus status;
    private String message;
    private String debugMessage;
    private HttpHeaders httpHeaders;
    private Instant timestamp;
    private String error;
    private String path;
    private String level;
    
    public ApiError() {
    	
    }
    
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public ApiError(HttpStatus status, String message, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ApiError(HttpStatus status, String message, String debugMessage) {
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }
    
    public ApiError(HttpStatus status, String message, HttpHeaders httpHeaders, String debugMessage) {
        this.status = status;
        this.message = message;
        this.httpHeaders = httpHeaders;
        this.debugMessage = debugMessage;
    }
    
    public ApiError(HttpStatus status, String message, HttpHeaders httpHeaders, String debugMessage,
    		Instant timestamp) {
        this.status = status;
        this.message = message;
        this.httpHeaders = httpHeaders;
        this.debugMessage = debugMessage;
        this.timestamp = timestamp;
    }
    
    public ApiError(HttpStatus status, String message, HttpHeaders httpHeaders, String debugMessage,
    		Instant timestamp, String error, String path) {
        this.status = status;
        this.message = message;
        this.httpHeaders = httpHeaders;
        this.debugMessage = debugMessage;
        this.timestamp = timestamp;
        this.error = error;
        this.path = path;
    }
    
    public ApiError(HttpStatus status, String message, HttpHeaders httpHeaders, String debugMessage,
    		Instant timestamp, String error, String path, String level) {
        this.status = status;
        this.message = message;
        this.httpHeaders = httpHeaders;
        this.debugMessage = debugMessage;
        this.timestamp = timestamp;
        this.error = error;
        this.path = path;
        this.level = level;
    }

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	
}


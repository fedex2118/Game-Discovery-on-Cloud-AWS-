package com.games.games_api.custom.response;

public class Message {

	private String text;
	private String statusCode;
	
	public Message(String text) {
		super();
		this.text = text;
	}
	
	public Message(String text, String statusCode) {
		super();
		this.text = text;
		this.statusCode = statusCode;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}

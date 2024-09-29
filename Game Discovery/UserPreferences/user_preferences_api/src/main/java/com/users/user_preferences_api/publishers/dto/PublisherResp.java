package com.users.user_preferences_api.publishers.dto;

public class PublisherResp {

	private Long userId;
	
	private Long publisherId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

}

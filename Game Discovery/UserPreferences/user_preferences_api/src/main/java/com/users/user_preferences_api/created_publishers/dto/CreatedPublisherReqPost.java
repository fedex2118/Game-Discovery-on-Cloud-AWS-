package com.users.user_preferences_api.created_publishers.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreatedPublisherReqPost {

	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long userId;
	
	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
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

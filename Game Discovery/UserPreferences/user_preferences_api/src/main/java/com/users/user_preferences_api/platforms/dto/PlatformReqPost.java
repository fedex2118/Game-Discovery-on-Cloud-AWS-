package com.users.user_preferences_api.platforms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PlatformReqPost {

	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long userId;
	
	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long platformId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}
	
	
}

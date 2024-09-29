package com.users.user_preferences_api.platforms.dto;

public class PlatformResp {

	private Long userId;
	
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

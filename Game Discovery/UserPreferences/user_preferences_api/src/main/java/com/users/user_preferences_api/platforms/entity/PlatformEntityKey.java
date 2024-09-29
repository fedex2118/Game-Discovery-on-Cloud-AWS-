package com.users.user_preferences_api.platforms.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PlatformEntityKey {
	
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "platform_id")
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

	@Override
	public int hashCode() {
		return Objects.hash(platformId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlatformEntityKey other = (PlatformEntityKey) obj;
		return Objects.equals(platformId, other.platformId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "PlatformEntityKey [userId=" + userId + ", platformId=" + platformId + "]";
	}

}

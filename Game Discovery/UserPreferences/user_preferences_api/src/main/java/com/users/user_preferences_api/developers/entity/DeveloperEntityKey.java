package com.users.user_preferences_api.developers.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DeveloperEntityKey {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "developer_id")
    private Long developerId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(developerId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeveloperEntityKey other = (DeveloperEntityKey) obj;
		return Objects.equals(developerId, other.developerId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "DeveloperEntityKey [userId=" + userId + ", developerId=" + developerId + "]";
	}
    
}

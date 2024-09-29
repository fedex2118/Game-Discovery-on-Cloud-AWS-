package com.users.user_preferences_api.publishers.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PublisherEntityKey {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "publisher_id")
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

	@Override
	public int hashCode() {
		return Objects.hash(publisherId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PublisherEntityKey other = (PublisherEntityKey) obj;
		return Objects.equals(publisherId, other.publisherId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "PublisherEntityKey [userId=" + userId + ", publisherId=" + publisherId + "]";
	}
    
}

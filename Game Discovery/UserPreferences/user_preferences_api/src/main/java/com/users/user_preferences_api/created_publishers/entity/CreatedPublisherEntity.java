package com.users.user_preferences_api.created_publishers.entity;

import java.util.Objects;

import com.users.user_preferences_api.preferences.entity.PreferenceEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_created_publishers")
public class CreatedPublisherEntity {
	
	@Id
	@Column(name = "publisher_id")
	private Long publisherId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private PreferenceEntity userPreference;

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public PreferenceEntity getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(PreferenceEntity userPreference) {
		this.userPreference = userPreference;
	}

	@Override
	public int hashCode() {
		return Objects.hash(publisherId, userPreference);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreatedPublisherEntity other = (CreatedPublisherEntity) obj;
		return Objects.equals(publisherId, other.publisherId) && Objects.equals(userPreference, other.userPreference);
	}
    
}

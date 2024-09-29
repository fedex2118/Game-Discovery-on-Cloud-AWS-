package com.users.user_preferences_api.created_developers.entity;

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
@Table(name = "user_created_developers")
public class CreatedDeveloperEntity {
	
	@Id
	@Column(name = "developer_id")
	private Long developerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private PreferenceEntity userPreference;

	public Long getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}

	public PreferenceEntity getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(PreferenceEntity userPreference) {
		this.userPreference = userPreference;
	}

	@Override
	public int hashCode() {
		return Objects.hash(developerId, userPreference);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreatedDeveloperEntity other = (CreatedDeveloperEntity) obj;
		return Objects.equals(developerId, other.developerId) && Objects.equals(userPreference, other.userPreference);
	}
    
}

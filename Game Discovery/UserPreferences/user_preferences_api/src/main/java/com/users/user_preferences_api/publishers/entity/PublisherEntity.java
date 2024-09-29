package com.users.user_preferences_api.publishers.entity;

import com.users.user_preferences_api.preferences.entity.PreferenceEntity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_pref_publishers")
public class PublisherEntity {

    @EmbeddedId
    private PublisherEntityKey id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private PreferenceEntity userPreference;

	public PublisherEntityKey getId() {
		return id;
	}

	public void setId(PublisherEntityKey id) {
		this.id = id;
	}

	public PreferenceEntity getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(PreferenceEntity userPreference) {
		this.userPreference = userPreference;
	}
    
    
}

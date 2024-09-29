package com.users.user_preferences_api.library.entity;

import com.users.user_preferences_api.preferences.entity.PreferenceEntity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_library")
public class LibraryEntity {

	@EmbeddedId
    private LibraryEntityKey id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private PreferenceEntity userPreference;

	public LibraryEntityKey getId() {
		return id;
	}

	public void setId(LibraryEntityKey id) {
		this.id = id;
	}

	public PreferenceEntity getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(PreferenceEntity userPreference) {
		this.userPreference = userPreference;
	}
    
}

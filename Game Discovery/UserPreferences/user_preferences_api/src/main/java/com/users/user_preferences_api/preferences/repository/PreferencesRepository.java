package com.users.user_preferences_api.preferences.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.user_preferences_api.preferences.entity.PreferenceEntity;

public interface PreferencesRepository extends JpaRepository<PreferenceEntity, Long> {

}

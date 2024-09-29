package com.users.user_preferences_api.created_games.entity;

import java.time.Instant;
import java.util.Objects;

import com.users.user_preferences_api.created_games.listener.TimestampListener;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_created_games")
@EntityListeners(TimestampListener.class)
public class CreatedGameEntity {
	
	@Id
	@Column(name = "game_id")
	private Long gameId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private PreferenceEntity userPreference;

	@Column(name = "updated_at")
    private Instant updatedAt;
	
    public CreatedGameEntity() {
    	
    }
	
	public CreatedGameEntity(Long gameId, PreferenceEntity userPreference) {
		super();
		this.gameId = gameId;
		this.userPreference = userPreference;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public PreferenceEntity getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(PreferenceEntity userPreference) {
		this.userPreference = userPreference;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameId, userPreference);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreatedGameEntity other = (CreatedGameEntity) obj;
		return Objects.equals(gameId, other.gameId) && Objects.equals(userPreference, other.userPreference);
	}
	
	
    
}

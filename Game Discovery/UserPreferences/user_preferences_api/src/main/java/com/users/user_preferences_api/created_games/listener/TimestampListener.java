package com.users.user_preferences_api.created_games.listener;

import java.time.Instant;

import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class TimestampListener {

    @PrePersist
    @PreUpdate
    public void setTimestamp(CreatedGameEntity gameEntity) {
        gameEntity.setUpdatedAt(Instant.now());
    }
}


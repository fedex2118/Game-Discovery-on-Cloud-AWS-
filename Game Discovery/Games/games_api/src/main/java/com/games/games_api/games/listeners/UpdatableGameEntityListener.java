package com.games.games_api.games.listeners;

import java.time.Instant;

import com.games.games_api.updatable_games.entity.UpdatableGameEntity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class UpdatableGameEntityListener {

    @PrePersist
    @PreUpdate
    public void setTimestamp(UpdatableGameEntity gameEntity) {
        gameEntity.setTimestamp(Instant.now());
    }
}

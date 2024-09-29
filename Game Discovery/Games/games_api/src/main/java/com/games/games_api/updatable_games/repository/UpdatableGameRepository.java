package com.games.games_api.updatable_games.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.games.games_api.updatable_games.entity.UpdatableGameEntity;

public interface UpdatableGameRepository extends JpaRepository<UpdatableGameEntity, Long> {

}


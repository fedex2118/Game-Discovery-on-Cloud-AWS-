package com.games.games_api.games.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.games.games_api.games.entity.GameEntity;

public interface GamesRepository extends JpaRepository<GameEntity, Long>, JpaSpecificationExecutor<GameEntity> {
	
	@Query("SELECT g.id FROM GameEntity g")
    List<Long> findAllGameIds();
}

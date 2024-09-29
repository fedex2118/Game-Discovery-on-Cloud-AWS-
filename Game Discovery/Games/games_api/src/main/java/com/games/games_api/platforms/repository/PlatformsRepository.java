package com.games.games_api.platforms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.games.games_api.platforms.entity.PlatformEntity;


public interface PlatformsRepository extends JpaRepository<PlatformEntity, Long> {

	Optional<PlatformEntity> findByName(String name);
}

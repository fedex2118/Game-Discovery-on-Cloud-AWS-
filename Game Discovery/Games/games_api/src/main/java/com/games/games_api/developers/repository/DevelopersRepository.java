package com.games.games_api.developers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.games.games_api.developers.entity.DeveloperEntity;


public interface DevelopersRepository extends JpaRepository<DeveloperEntity, Long>, JpaSpecificationExecutor<DeveloperEntity> {
	
	Optional<DeveloperEntity> findByName(String name);
}

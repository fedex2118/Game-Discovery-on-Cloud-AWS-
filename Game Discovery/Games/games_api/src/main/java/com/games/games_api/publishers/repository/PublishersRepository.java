package com.games.games_api.publishers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.games.games_api.publishers.entity.PublisherEntity;

public interface PublishersRepository extends JpaRepository<PublisherEntity, Long>, JpaSpecificationExecutor<PublisherEntity> {
	
	Optional<PublisherEntity> findByName(String name);
	
}

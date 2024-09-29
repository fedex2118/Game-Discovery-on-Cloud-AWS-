package com.games.games_api.genres.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.games.games_api.genres.entity.GenreEntity;


public interface GenresRepository extends JpaRepository<GenreEntity, Long> {

	Optional<GenreEntity> findByName(String name);
}

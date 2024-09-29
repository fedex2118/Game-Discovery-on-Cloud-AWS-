package com.users.user_preferences_api.genres.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.users.user_preferences_api.genres.entity.GenreEntity;
import com.users.user_preferences_api.genres.entity.GenreEntityKey;

public interface GenresRepository extends JpaRepository<GenreEntity, GenreEntityKey> {

	List<GenreEntity> findAllByIdGenreId(Long genreId);
	List<GenreEntity> findAllByIdUserId(Long userId);
	Page<GenreEntity> findAllByIdUserId(Long userId, Pageable pageable);
}

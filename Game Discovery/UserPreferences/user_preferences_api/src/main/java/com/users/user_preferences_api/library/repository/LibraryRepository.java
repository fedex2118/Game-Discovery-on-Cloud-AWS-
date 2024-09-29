package com.users.user_preferences_api.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.users.user_preferences_api.library.entity.LibraryEntity;
import com.users.user_preferences_api.library.entity.LibraryEntityKey;

public interface LibraryRepository extends JpaRepository<LibraryEntity, LibraryEntityKey> {

	List<LibraryEntity> findAllByIdGameId(Long gameId);
	List<LibraryEntity> findAllByIdUserId(Long userId);
	Page<LibraryEntity> findAllByIdUserId(Long userId, Pageable pageable);
}

package com.users.user_preferences_api.created_games.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;

public interface CreatedGameRepository extends JpaRepository<CreatedGameEntity, Long>, JpaSpecificationExecutor<CreatedGameEntity> {

    Page<CreatedGameEntity> findAllByUserPreferenceId(Long userId, Pageable pageable);
    List<CreatedGameEntity> findAllByUserPreferenceId(Long userId);
    Optional<CreatedGameEntity> findByGameIdAndUserPreferenceId(Long gameId, Long userId);
    List<CreatedGameEntity> findAllByGameId(Long gameId);
}

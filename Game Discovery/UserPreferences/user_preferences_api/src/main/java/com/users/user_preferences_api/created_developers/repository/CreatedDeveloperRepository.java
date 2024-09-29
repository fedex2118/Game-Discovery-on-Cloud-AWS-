package com.users.user_preferences_api.created_developers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.users.user_preferences_api.created_developers.entity.CreatedDeveloperEntity;

public interface CreatedDeveloperRepository extends JpaRepository<CreatedDeveloperEntity, Long>, JpaSpecificationExecutor<CreatedDeveloperEntity> {

    Page<CreatedDeveloperEntity> findAllByUserPreferenceId(Long userId, Pageable pageable);
    List<CreatedDeveloperEntity> findAllByUserPreferenceId(Long userId);
    Optional<CreatedDeveloperEntity> findByDeveloperIdAndUserPreferenceId(Long developerId, Long userId);
    List<CreatedDeveloperEntity> findAllByDeveloperId(Long developerId);
}

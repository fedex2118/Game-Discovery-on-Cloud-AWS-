package com.users.user_preferences_api.created_publishers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.users.user_preferences_api.created_publishers.entity.CreatedPublisherEntity;

public interface CreatedPublisherRepository extends JpaRepository<CreatedPublisherEntity, Long>, JpaSpecificationExecutor<CreatedPublisherEntity> {

    Page<CreatedPublisherEntity> findAllByUserPreferenceId(Long userId, Pageable pageable);
    List<CreatedPublisherEntity> findAllByUserPreferenceId(Long userId);
    Optional<CreatedPublisherEntity> findByPublisherIdAndUserPreferenceId(Long publisherId, Long userId);
    List<CreatedPublisherEntity> findAllByPublisherId(Long developerId);
}

package com.users.user_preferences_api.developers.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.users.user_preferences_api.developers.entity.DeveloperEntity;
import com.users.user_preferences_api.developers.entity.DeveloperEntityKey;

public interface DevelopersRepository extends JpaRepository<DeveloperEntity, DeveloperEntityKey> {

	List<DeveloperEntity> findAllByIdDeveloperId(Long developerId);
	List<DeveloperEntity> findAllByIdUserId(Long userId);
	Page<DeveloperEntity> findAllByIdUserId(Long userId, Pageable pageable);
}

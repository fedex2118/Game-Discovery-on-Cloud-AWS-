package com.users.user_preferences_api.platforms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.users.user_preferences_api.platforms.entity.PlatformEntity;
import com.users.user_preferences_api.platforms.entity.PlatformEntityKey;

public interface PlatformsRepository extends JpaRepository<PlatformEntity, PlatformEntityKey> {

	List<PlatformEntity> findAllByIdPlatformId(Long platformId);
	List<PlatformEntity> findAllByIdUserId(Long userId);
	Page<PlatformEntity> findAllByIdUserId(Long userId, Pageable pageable);
}

package com.users.user_preferences_api.publishers.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.users.user_preferences_api.publishers.entity.PublisherEntity;
import com.users.user_preferences_api.publishers.entity.PublisherEntityKey;

public interface PublishersRepository extends JpaRepository<PublisherEntity, PublisherEntityKey> {

	List<PublisherEntity> findAllByIdPublisherId(Long publisherId);
	List<PublisherEntity> findAllByIdUserId(Long userId);
	Page<PublisherEntity> findAllByIdUserId(Long userId, Pageable pageable);
}

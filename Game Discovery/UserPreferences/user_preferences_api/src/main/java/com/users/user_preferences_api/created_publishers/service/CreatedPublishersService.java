package com.users.user_preferences_api.created_publishers.service;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperResp;
import com.users.user_preferences_api.created_developers.entity.CreatedDeveloperEntity;
import com.users.user_preferences_api.created_publishers.dto.CreatedPublisherReqPost;
import com.users.user_preferences_api.created_publishers.dto.CreatedPublisherResp;
import com.users.user_preferences_api.created_publishers.entity.CreatedPublisherEntity;
import com.users.user_preferences_api.created_publishers.mapper.CreatedPublisherMapper;
import com.users.user_preferences_api.created_publishers.repository.CreatedPublisherRepository;
import com.users.user_preferences_api.custom.exception.UserReadableException;
import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class CreatedPublishersService implements ICreatedPublishersService {
	
	private static final Logger logger = LoggerFactory.getLogger(CreatedPublishersService.class);
	
	@Autowired
	private CreatedPublisherRepository createdPublisherRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private CreatedPublisherMapper createdPublisherMapper = Mappers.getMapper(CreatedPublisherMapper.class);
	
	@Override
	public CollectionResponse<CreatedPublisherResp> create(CreatedPublisherReqPost createdPublisherReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(createdPublisherReqPost.getUserId())
				.orElseThrow(() -> new UserReadableException("User preference not found for user with ID: " 
					+ createdPublisherReqPost.getUserId()));
		
		// save the entity
		CreatedPublisherEntity createdPublisherEntity = new CreatedPublisherEntity();
		createdPublisherEntity.setPublisherId(createdPublisherReqPost.getPublisherId());
		createdPublisherEntity.setUserPreference(userPreference);
		
		createdPublisherEntity = createdPublisherRepository.save(createdPublisherEntity);
		
		CreatedPublisherResp response = createdPublisherMapper.toCreatedPublisherResponse(createdPublisherEntity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	public CollectionResponse<CreatedPublisherResp> update(Long publisherId, Long userId) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(userId)
				.orElseThrow();
		
		// save the entity
		CreatedPublisherEntity createdPublisherEntity = createdPublisherRepository.findById(publisherId)
				.orElseThrow(() -> new UserReadableException("Created publisher not found with ID: " + publisherId));
		
		createdPublisherEntity.setUserPreference(userPreference);
		
		createdPublisherEntity = createdPublisherRepository.save(createdPublisherEntity);
		
		CreatedPublisherResp response = createdPublisherMapper.toCreatedPublisherResponse(createdPublisherEntity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "createdPublishersCache", key = "#publisherId + '_' + #userId" )
	public CollectionResponse<CreatedPublisherResp> findByKey(Long publisherId, Long userId) {
		CreatedPublisherEntity createdPublisherFound = createdPublisherRepository.findByPublisherIdAndUserPreferenceId(publisherId, userId)
				.orElseThrow(() -> new UserReadableException("Created publisher not found with ID: " + publisherId + " and User ID: " + userId));
		
		logger.info(LoggerConstants.CREATED_PUBLISHER_ID_FOUND, createdPublisherFound.getPublisherId());
		CreatedPublisherResp response = createdPublisherMapper.toCreatedPublisherResponse(createdPublisherFound);
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "createdDevelopersCache", key = "#developerId + '_' + #userId" )
	public CollectionResponse<CreatedPublisherResp> findByUserId(Long userId) {
		List<CreatedPublisherEntity> createdDeveloperFound = createdPublisherRepository.findAllByUserPreferenceId(userId);
		
		if(!createdDeveloperFound.isEmpty()) {
			//logger.info(LoggerConstants.CREATED_DEVELOPER_ID_FOUND, createdDeveloperFound.getDeveloperId());
			List<CreatedPublisherResp> response = createdPublisherMapper.toCreatedPublishersResponse(createdDeveloperFound);
			return new CollectionResponse<>(response);
		}
		

		return new CollectionResponse<>();
	}
	
	@Override
	//@CacheEvict(value = "createdPublishersCache", key = "#publisherId + '_' + #userId")
	@Transactional
	public CollectionResponse<CreatedPublisherResp> deleteByKey(Long publisherId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		CreatedPublisherEntity createdPublisherEntity = createdPublisherRepository.findByPublisherIdAndUserPreferenceId(publisherId, userId)
				.orElseThrow(() -> new UserReadableException("Created publisher not found with ID: " + publisherId + " and User ID: " + userId));
		
		createdPublisherRepository.delete(createdPublisherEntity);
		
		return new CollectionResponse<>();
	}

}

package com.users.user_preferences_api.created_developers.service;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperReqPost;
import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperResp;
import com.users.user_preferences_api.created_developers.entity.CreatedDeveloperEntity;
import com.users.user_preferences_api.created_developers.mapper.CreatedDeveloperMapper;
import com.users.user_preferences_api.created_developers.repository.CreatedDeveloperRepository;
import com.users.user_preferences_api.custom.exception.UserReadableException;
import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class CreatedDevelopersService implements ICreatedDevelopersService {
	
	private static final Logger logger = LoggerFactory.getLogger(CreatedDevelopersService.class);
	
	@Autowired
	private CreatedDeveloperRepository createdDeveloperRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private CreatedDeveloperMapper createdDeveloperMapper = Mappers.getMapper(CreatedDeveloperMapper.class);
	
	@Override
	public CollectionResponse<CreatedDeveloperResp> create(CreatedDeveloperReqPost createdDeveloperReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(createdDeveloperReqPost.getUserId())
				.orElseThrow(() -> new UserReadableException("User preference not found for user with ID: " 
					+ createdDeveloperReqPost.getUserId()));
		
		// save the entity
		CreatedDeveloperEntity createdDeveloperEntity = new CreatedDeveloperEntity();
		createdDeveloperEntity.setDeveloperId(createdDeveloperReqPost.getDeveloperId());
		createdDeveloperEntity.setUserPreference(userPreference);
		
		createdDeveloperEntity = createdDeveloperRepository.save(createdDeveloperEntity);
		
		CreatedDeveloperResp response = createdDeveloperMapper.toCreatedDeveloperResponse(createdDeveloperEntity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	public CollectionResponse<CreatedDeveloperResp> update(Long developerId, Long userId) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(userId)
				.orElseThrow();
		
		// save the entity
		CreatedDeveloperEntity createdDeveloperEntity = createdDeveloperRepository.findById(developerId)
				.orElseThrow(() -> new UserReadableException("Created developer not found with ID: " + developerId));
		
		createdDeveloperEntity.setUserPreference(userPreference);
		
		createdDeveloperEntity = createdDeveloperRepository.save(createdDeveloperEntity);
		
		CreatedDeveloperResp response = createdDeveloperMapper.toCreatedDeveloperResponse(createdDeveloperEntity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "createdDevelopersCache", key = "#developerId + '_' + #userId" )
	public CollectionResponse<CreatedDeveloperResp> findByKey(Long developerId, Long userId) {
		CreatedDeveloperEntity createdDeveloperFound = createdDeveloperRepository.findByDeveloperIdAndUserPreferenceId(developerId, userId)
				.orElseThrow(() -> new UserReadableException("Created developer not found with ID: " + developerId + " and User ID: " + userId));
		
		logger.info(LoggerConstants.CREATED_DEVELOPER_ID_FOUND, createdDeveloperFound.getDeveloperId());
		CreatedDeveloperResp response = createdDeveloperMapper.toCreatedDeveloperResponse(createdDeveloperFound);
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "createdDevelopersCache", key = "#developerId + '_' + #userId" )
	public CollectionResponse<CreatedDeveloperResp> findByUserId(Long userId) {
		List<CreatedDeveloperEntity> createdDeveloperFound = createdDeveloperRepository.findAllByUserPreferenceId(userId);
		
		if(!createdDeveloperFound.isEmpty()) {
			//logger.info(LoggerConstants.CREATED_DEVELOPER_ID_FOUND, createdDeveloperFound.getDeveloperId());
			List<CreatedDeveloperResp> response = createdDeveloperMapper.toCreatedDevelopersResponse(createdDeveloperFound);
			return new CollectionResponse<>(response);
		}
		

		return new CollectionResponse<>();
	}
	
	@Override
	//@CacheEvict(value = "createdDevelopersCache", key = "#developerId + '_' + #userId")
	@Transactional
	public CollectionResponse<CreatedDeveloperResp> deleteByKey(Long developerId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		CreatedDeveloperEntity createdDeveloperEntity = createdDeveloperRepository.findByDeveloperIdAndUserPreferenceId(developerId, userId)
				.orElseThrow(() -> new UserReadableException("Created developer not found with ID: " + developerId + " and User ID: " + userId));
		
		createdDeveloperRepository.delete(createdDeveloperEntity);
		
		return new CollectionResponse<>();
	}

}

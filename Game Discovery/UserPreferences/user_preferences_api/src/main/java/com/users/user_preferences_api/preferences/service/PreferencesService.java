package com.users.user_preferences_api.preferences.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;
import com.users.user_preferences_api.created_games.repository.CreatedGameRepository;
import com.users.user_preferences_api.custom.exception.UserReadableException;
import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPost;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPut;
import com.users.user_preferences_api.preferences.dto.PreferenceResp;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.mapper.PreferencesMapper;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class PreferencesService implements IPreferencesService {
	
	private static final Logger logger = LoggerFactory.getLogger(PreferencesService.class);

	@Autowired
	private PreferencesRepository preferencesRepository;
	
	@Autowired
	private CreatedGameRepository createdGameRepository;
	
	private PreferencesMapper preferenceMapper = Mappers.getMapper(PreferencesMapper.class);
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "preferenceCache", key = "#id" )
	public CollectionResponse<PreferenceResp> findById(Long id) {
		PreferenceEntity prefEntityFound = preferencesRepository.findById(id).orElse(null);

		if (prefEntityFound != null) {
			logger.info(LoggerConstants.USER_ID_FOUND, prefEntityFound);
			PreferenceResp response = preferenceMapper.toResponse(prefEntityFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "preferenceCache", key = "#id" )
	public CollectionResponse<Long> findUserIdByOwnedGame(Long gameId) {
		CreatedGameEntity createdGameEntityFound = createdGameRepository.findById(gameId).orElse(null);

		if (createdGameEntityFound != null) {
			Long userId = createdGameEntityFound.getUserPreference().getId();
			logger.info(LoggerConstants.USER_ID_FOUND, userId);
			
			return new CollectionResponse<>(userId);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	public CollectionResponse<PreferenceResp> create(PreferenceReqPost preferenceReqPost) {
		
		// check if entity already exists
		PreferenceEntity prefEntityFound = preferencesRepository.findById(preferenceReqPost.getId()).orElse(null);
		
		if(prefEntityFound != null) {
			throw new UserReadableException("User with ID: " + preferenceReqPost.getId()
			+  " already exists", "400");
		}
		
		// handle starting average rating variable
		BigDecimal startingAverageRating = preferenceReqPost.getStartingAverageRating();
		if(startingAverageRating != null) {
			startingAverageRating = startingAverageRating.setScale(2, RoundingMode.HALF_UP);
			preferenceReqPost.setStartingAverageRating(startingAverageRating);
		}
		
		final PreferenceEntity prefEntity = preferenceMapper.toEntity(preferenceReqPost);
		
		Set<CreatedGameEntity> gamesToCreate = new HashSet<>();
		
		if(!CollectionUtils.isEmpty(preferenceReqPost.getCreatedGamesIds())) {
			preferenceReqPost.getCreatedGamesIds()
			.forEach(el -> gamesToCreate.add(new CreatedGameEntity(el, prefEntity)));
			
			prefEntity.setCreatedGames(gamesToCreate);
		}
		
		PreferenceEntity prefEntitySaved = preferencesRepository.save(prefEntity);
		
		PreferenceResp response = preferenceMapper.toResponse(prefEntitySaved);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	//@CacheEvict(value = "preferenceCache", key = "#id")
	public CollectionResponse<PreferenceResp> update(Long id, PreferenceReqPut preferenceReqPut) {
		
		// entity must exist
		final PreferenceEntity entity = preferencesRepository.findById(id).orElseThrow();
		
		// handle starting average rating variable
		BigDecimal startingAverageRating = preferenceReqPut.getStartingAverageRating();
		if(startingAverageRating != null) {
			startingAverageRating = startingAverageRating.setScale(2, RoundingMode.HALF_UP);
			preferenceReqPut.setStartingAverageRating(startingAverageRating);
		}
		
		// convert request to entity
		preferenceMapper.toPreferenceEntity(preferenceReqPut, entity);
		
		// check if there is any game to add
		Set<CreatedGameEntity> gamesToCreate = new HashSet<>();
		
		if(!CollectionUtils.isEmpty(preferenceReqPut.getCreatedGamesIds())) {
			preferenceReqPut.getCreatedGamesIds()
			.forEach(el -> gamesToCreate.add(new CreatedGameEntity(el, entity)));
			
			entity.getCreatedGames().retainAll(gamesToCreate);
			entity.getCreatedGames().addAll(gamesToCreate);
		}
		
		if(preferenceReqPut.getCreatedGamesIds() != null && preferenceReqPut.getCreatedGamesIds().isEmpty()) {
			// massive delete of created games
			entity.getCreatedGames().removeAll(entity.getCreatedGames());
		}
		
		// save entity
		PreferenceEntity prefEntitySaved = preferencesRepository.save(entity);
		
		// response
		PreferenceResp response = preferenceMapper.toResponse(prefEntitySaved);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	//@CacheEvict(value = "preferenceCache", key = "#id")
	public CollectionResponse<PreferenceResp> deleteById(Long id) {
		
		// if it doesn't exist no blocking
		//preferencesRepository.findById(id).orElseThrow();
		
		// delete the preference entity
		preferencesRepository.deleteById(id);
		
		return new CollectionResponse<>();
	}
	
}

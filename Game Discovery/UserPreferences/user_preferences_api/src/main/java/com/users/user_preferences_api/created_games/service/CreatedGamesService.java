package com.users.user_preferences_api.created_games.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.users.user_preferences_api.created_games.dto.CreatedGameReqPost;
import com.users.user_preferences_api.created_games.dto.CreatedGameResp;
import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;
import com.users.user_preferences_api.created_games.mapper.CreatedGameMapper;
import com.users.user_preferences_api.created_games.repository.CreatedGameRepository;
import com.users.user_preferences_api.created_games.specifications.CreatedGamesSpecifications;
import com.users.user_preferences_api.custom.exception.UserReadableException;
import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class CreatedGamesService implements ICreatedGamesService {
	
	private static final Logger logger = LoggerFactory.getLogger(CreatedGamesService.class);
	
	@Autowired
	private CreatedGameRepository createdGameRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private CreatedGameMapper createdGameMapper = Mappers.getMapper(CreatedGameMapper.class);
	
	@Override
	public CollectionResponse<CreatedGameResp> create(CreatedGameReqPost createdGameReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(createdGameReqPost.getUserId())
				.orElseThrow(() -> new UserReadableException("User preference not found for user with ID: " 
					+ createdGameReqPost.getUserId()));
		
		// save the entity
		CreatedGameEntity createdGameEntity = new CreatedGameEntity();
		createdGameEntity.setGameId(createdGameReqPost.getGameId());
		createdGameEntity.setUserPreference(userPreference);
		
		createdGameEntity = createdGameRepository.save(createdGameEntity);
		
		CreatedGameResp response = createdGameMapper.toCreatedGameResponse(createdGameEntity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	public CollectionResponse<CreatedGameResp> update(Long gameId, Long userId) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(userId)
				.orElseThrow();
		
		// save the entity
		CreatedGameEntity createdGameEntity = createdGameRepository.findById(gameId)
				.orElseThrow(() -> new UserReadableException("Created game not found with ID: " + gameId));
		
		createdGameEntity.setUserPreference(userPreference);
		
		createdGameEntity = createdGameRepository.save(createdGameEntity);
		
		CreatedGameResp response = createdGameMapper.toCreatedGameResponse(createdGameEntity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "createdGamesCache", key = "#gameId + '_' + #userId" )
	public CollectionResponse<CreatedGameResp> findByKey(Long gameId, Long userId) {
		CreatedGameEntity createdGameFound = createdGameRepository.findByGameIdAndUserPreferenceId(gameId, userId)
				.orElseThrow(() -> new UserReadableException("Created game not found with ID: " + gameId + " and User ID: " + userId));
		
		logger.info(LoggerConstants.CREATED_GAME_ID_FOUND, createdGameFound.getGameId());
		CreatedGameResp response = createdGameMapper.toCreatedGameResponse(createdGameFound);
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<CreatedGameResp> findAllByUserId(Long userId, Pageable pageable) {
		Page<CreatedGameEntity> gamesFound = createdGameRepository.findAllByUserPreferenceId(userId, pageable);
		
		List<CreatedGameResp> response = createdGameMapper.toCreatedGamesResponse(gamesFound.getContent());
		
        // convert it for pagination
        Page<CreatedGameResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());
		return new CollectionResponse<>(responsePage);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<CreatedGameResp> findAllByUserIdLastFortyEightHours(Long userId, Pageable pageable) {
		Specification<CreatedGameEntity> spec = Specification.where(null);
		
		// look for the user games created in the last 48 hours, the lookup is limited to 10 elements for performance
		spec = spec.and(CreatedGamesSpecifications.isUpdatedAt(Instant.now().minus(Duration.ofHours(48))))
				   .and(CreatedGamesSpecifications.byUserId(userId));
		
		Page<CreatedGameEntity> gamesFound = createdGameRepository.findAll(spec, pageable);
		
		List<CreatedGameResp> response = createdGameMapper.toCreatedGamesResponse(gamesFound.getContent());
		
        // convert it for pagination
        Page<CreatedGameResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());
		return new CollectionResponse<>(responsePage);
	}
	
	@Override
	//@CacheEvict(value = "createdGamesCache", key = "#gameId + '_' + #userId")
	@Transactional
	public CollectionResponse<CreatedGameResp> deleteByKey(Long gameId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		CreatedGameEntity createdGameEntity = createdGameRepository.findByGameIdAndUserPreferenceId(gameId, userId)
				.orElseThrow(() -> new UserReadableException("Created game not found with ID: " + gameId + " and User ID: " + userId));
		
		createdGameRepository.delete(createdGameEntity);
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<CreatedGameResp> deleteUserGames(Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		// first find all games for the input user
		List<CreatedGameEntity> gamesFound = createdGameRepository.findAllByUserPreferenceId(userId);
		
		// delete all found game entries
		createdGameRepository.deleteAll(gamesFound);
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<CreatedGameResp> deleteGames(Long gameId) {

	    // first find all games with input ID
	    List<CreatedGameEntity> gamesFound = createdGameRepository.findAllByGameId(gameId);

	    // delete all found game entries
	    if (!gamesFound.isEmpty()) {
	    	createdGameRepository.deleteAll(gamesFound);
	    }
	    
	    return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<CreatedGameResp> deleteGamesWithIds(List<Long> gameIds) {
		List<CreatedGameEntity> gamesFound = createdGameRepository.findAllById(gameIds);
		
		if (!gamesFound.isEmpty()) {
	    	createdGameRepository.deleteAll(gamesFound);
	    }
	    
	    return new CollectionResponse<>();
	}

}

package com.gateway.gateway_api.games.services;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.cache.PreferenceCacheService;
import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.games.data.classes.GameCriteria;
import com.gateway.gateway_api.games.data.classes.GameReqPost;
import com.gateway.gateway_api.games.data.classes.GameReqPut;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GameStatus;
import com.gateway.gateway_api.games.data.classes.GameStatusResp;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPost;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;
import com.gateway.gateway_api.games.data.classes.UpdatableGameResp;
import com.gateway.gateway_api.games.mapper.UpdatableGamesMapper;
import com.gateway.gateway_api.games.request.dto.GamePutDevsAndPublishers;
import com.gateway.gateway_api.preferences.data.classes.CreatedGameReqPost;
import com.gateway.gateway_api.preferences.data.classes.CreatedGameResp;
import com.gateway.gateway_api.preferences.data.classes.PreferenceReqPost;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.users.data.classes.UserResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

@Service
public class GamesService implements IGamesService {

	private static final Integer MAX_PAGE_SIZE = 10;
	
	private static final Integer MAX_PENDING_SUBMISSIONS = 10;

	private static final Logger logger = LoggerFactory.getLogger(GamesService.class);
	
	private final UpdatableGamesMapper updatableGamesMapper = Mappers.getMapper(UpdatableGamesMapper.class);

	@Autowired
	private UsersRequesterService usersRequesterService;

	@Autowired
	private GamesRequesterService gamesRequesterService;

	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private PreferenceCacheService preferenceCacheService;
	
	@Autowired
	private UpdatableGameService updatableGameService;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	public CollectionResponse<GameResp> findById(Long id) {
		
		CollectionResponse<GameResp> cacheResponse = this.handleCacheResponse(id);
		
		if(cacheResponse != null) {
			return cacheResponse;
		}

		CollectionResponse<GameResp> response = gamesRequesterService.findGameById(id);
		
		this.handleCacheInsertion(id, response);

		return response;
	}

	@Override
	public CollectionResponse<GameResp> findByCriteria(GameCriteria criteria, Pageable pageable) {

		// check that pagination doesn't exceed the limit size
		if (pageable.getPageSize() > MAX_PAGE_SIZE) {
			throw new UserReadableException("Page size must be equal or less than: " + MAX_PAGE_SIZE, "400");
		}

		MultiValueMap<String, String> params = CustomUtils.convertParams(criteria, pageable);

		CollectionResponse<GameResp> response = gamesRequesterService.findGames(params);

		return response;

	}

	@Override
	public CollectionResponse<GameResp> create(GameReqPost gameReqPost) {

		// find user id
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// limit the amount of games the user can submit
		// look for the last 10 user games submitted on the last 48 hours
		CollectionResponse<CreatedGameResp> userLastSubmittedGames = preferencesRequesterService.findAllByUserIdLastFortyEightHours(userRespId.getId());
		
		List<CreatedGameResp> submittedGames = userLastSubmittedGames.getContent().stream().toList();
		
		// if there are less than 10 submissions there is no need to check on the pending status
		if(submittedGames.size() >= MAX_PENDING_SUBMISSIONS) {
			// of the created games found now look up for their status
			List<String> gameIdsToString = submittedGames.stream().map(el -> el.getGameId().toString()).toList();
			CollectionResponse<Long> pendingGamesIds = gamesRequesterService.getPendingGamesIds(gameIdsToString);
			if(pendingGamesIds.getContent().size() == MAX_PENDING_SUBMISSIONS) {
				throw new UserReadableException("Every forty eight hours the maximum of games that can be submitted without approval is "
						+ MAX_PENDING_SUBMISSIONS + ". Your games will be approved or rejected during the next twenty four hours. Wait some time before retry.", "403");
			}
		}

		// create game first
		CollectionResponse<GameResp> response = gamesRequesterService.create(gameReqPost);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}

		GameResp gameCreated = response.getContent().stream().findFirst().orElse(null);
		
		this.handleCacheEviction(gameCreated.getId());

		String customErrorMessage = "A game with id: " + gameCreated.getId()
				+ " was created successfully but there was a problem with adding it to user preference. "
				+ "Check for assistance.";

		// check if preference exist
		CollectionResponse<PreferenceResp> preferenceFound = preferencesRequesterService.findById(userRespId.getId());

		// handle errors
		if (preferenceFound.getContent().isEmpty() && !preferenceFound.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(preferenceFound, customErrorMessage);
		}

		if (preferenceFound.getContent().isEmpty()) {
			// it means that user has no preference created yet
			// create a new preference
			PreferenceReqPost newPreference = new PreferenceReqPost(userRespId.getId());

			CollectionResponse<PreferenceResp> preferenceCreated = preferencesRequesterService.create(newPreference);

			// handle errors
			if (preferenceCreated.getContent().isEmpty() && !preferenceCreated.getMessages().isEmpty()) {
				// attempt deleting the created game
				CollectionResponse<GameResp> gameDeletion = gamesRequesterService.delete(gameCreated.getId());
				// if there are errors retrieve the game id but throw an error message
				if (!gameDeletion.getMessages().isEmpty()) {
					throw new UserReadableException(customErrorMessage, "500");
				}
			}
			
		}
		
		// wipe value on preference cache if it exists
		try {
			preferenceCacheService.evictPreferenceId(userRespId.getId());
		} catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}

		// add the game id to the created games preference list
		CreatedGameReqPost submittedGameToAdd = new CreatedGameReqPost();
		submittedGameToAdd.setGameId(gameCreated.getId());
		submittedGameToAdd.setUserId(userRespId.getId());

		// update the preference
		CollectionResponse<CreatedGameResp> createdGameResp = preferencesRequesterService.createUserCreatedGame(submittedGameToAdd);

		// handle errors
		if (createdGameResp.getContent().isEmpty() && !createdGameResp.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(createdGameResp, customErrorMessage);
		}

		return response;
	}

	@Override
	public CollectionResponse<UpdatableGameResp> sendUpdateSuggestion(Long id, UpdatableGameReqPut gameReqPut) {

		// find user id
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);

		// the user must own the game
		CollectionResponse<CreatedGameResp> createdGameFoundResp = preferencesRequesterService.findCreatedGameByKey(id,
				userRespId.getId());

		// handle errors
		if (createdGameFoundResp.getContent().isEmpty() && !createdGameFoundResp.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(createdGameFoundResp);
		}

		// check if user is authorized
		if (createdGameFoundResp.getContent().isEmpty()) {
			throw new UserReadableException("Unauthorized", "401");
		}
		
		// check that the status is not rejected
		CollectionResponse<GameStatusResp> gameStatusResp = gamesRequesterService.findGameStatus(id);
		
		if(gameStatusResp.getContent().isEmpty()) {
			throw new UserReadableException("Game with ID: " + id + " not found.");
		}
		
		GameStatusResp gameStatus = gameStatusResp.getContent().stream().findFirst().orElse(null);
		
		if(gameStatus.getStatus().getCode() == GameStatus.REJECTED.getCode()) {
			throw new UserReadableException("Game with ID: " + id + " has been rejected. Ask for assistance or submit a new game.");
		}
		
		// check that game is not pending
		if(gameStatus.getStatus().getCode() == GameStatus.PENDING.getCode()) {
			throw new UserReadableException("Game with ID: " + id + " is still on pending status. Before being able to update it wait for approval.");
		}
		
		// clean game cache key before
		this.handleCacheEviction(id);
		
		// wipe value on preference cache if it exists.
		// this must be done to have new created games 'updated_at' timestamps
		try {
			preferenceCacheService.evictPreferenceId(userRespId.getId());
		} catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
		
		// proceed submitting the updatable game without actually updating the current game
		// the game will be updated only by an administrator if the update is approved.
		// find if an updatable game already exists
		CollectionResponse<UpdatableGameResp> updatableGameFound = gamesRequesterService.findUpdatableGameById(id);
		
		CollectionResponse<UpdatableGameResp> response = new CollectionResponse<>();
		
		if(updatableGameFound.getContent().isEmpty()) {
			// this means we need to create a new entry
			UpdatableGameReqPost postRequest = updatableGamesMapper.toUpdatableGameReqPost(gameReqPut);
			postRequest.setId(id);
			
			response = gamesRequesterService.createUpdatableGame(postRequest);
			
		} else {
			// this means we need to update the existing entry
			response = gamesRequesterService.updateUpdatableGame(id, gameReqPut);
			
			// evict cache entry
			updatableGameService.handleCacheEviction(id);
		}

		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}

		return response;
	}
	
	@Override
	public CollectionResponse<GameResp> updateGameDevsAndPublishers(Long id, GamePutDevsAndPublishers request) {
		// find user id
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// find if the user owns the game
		CollectionResponse<CreatedGameResp> createdGameRespFound = preferencesRequesterService.findCreatedGameByKey(id, userRespId.getId());
		
		if(createdGameRespFound.getContent().isEmpty()) {
			throw new UserReadableException("Unauthorized", "401");
		}
		
		// find the status of the game
		CollectionResponse<GameStatusResp> statusResp = gamesRequesterService.findGameStatus(id);
		
		if(!statusResp.getContent().isEmpty()) {
			GameStatusResp status = statusResp.getContent().stream().findFirst().orElse(null);
			if(status.getStatus().getCode() != GameStatus.APPROVED.getCode()) {
				throw new UserReadableException("Game must be approved first to be updated", "400");
			}
		}
		
		// map game devs and publishers
		GameReqPut putRequest = new GameReqPut();
		putRequest.setDeveloperIds(request.getDeveloperIds());
		putRequest.setPublisherIds(request.getPublisherIds());
		
		CollectionResponse<GameResp> response = gamesRequesterService.update(id, putRequest);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// update games cache
		this.handleCacheEviction(id);
		
		// wipe value on preference cache if it exists.
		// this must be done to have new created games 'updated_at' timestamps
		try {
			preferenceCacheService.evictPreferenceId(userRespId.getId());
		} catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
		
		GameResp gameUpdated = response.getContent().stream().findFirst().orElse(null);
		
		String customErrorMessage = "Game with id: " + gameUpdated.getId()
		+ " was updated successfully but there was a problem with updating the user preference. "
		+ "Check for assistance.";
		
		// update the preference
		CollectionResponse<CreatedGameResp> createdGameResp = preferencesRequesterService.updateUserCreatedGame(id, userRespId.getId());
		
		// handle errors
		if (createdGameResp.getContent().isEmpty() && !createdGameResp.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(createdGameResp, customErrorMessage);
		}
		
		return response;
	}
	
    private CollectionResponse<GameResp> handleCacheResponse(Long gameId) {
        try {
            // Perform cache eviction or other Redis operations here
        	 ValueWrapper valueWrapper =  cacheManager.getCache("gameCache").get(gameId);
        	 if(valueWrapper != null) {
        		 Object value = valueWrapper.get();
        		 if(value instanceof CollectionResponse) {
        			 return (CollectionResponse<GameResp>) value;
        		 }
        	 }
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
        return null;
    }
    
    public void handleCacheInsertion(Long gameId, CollectionResponse<GameResp> responseDB) {
        try {
            // Perform cache eviction or other Redis operations here
        	 cacheManager.getCache("gameCache").putIfAbsent(gameId, responseDB);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
	
    public void handleCacheEviction(Long gameId) {
        try {
            // Perform cache eviction or other Redis operations here
        	cacheManager.getCache("gameCache").evict(gameId);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }

}

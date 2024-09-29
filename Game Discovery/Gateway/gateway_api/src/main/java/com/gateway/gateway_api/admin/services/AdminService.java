package com.gateway.gateway_api.admin.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.gateway.gateway_api.application.AppProperties;
import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.cache.CacheSynchronizationService;
import com.gateway.gateway_api.custom.cache.GameStatsCacheService;
import com.gateway.gateway_api.custom.cache.PreferenceCacheService;
import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.response.Message;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPatch;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPost;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.GameReqPatchStatus;
import com.gateway.gateway_api.games.data.classes.GameReqPut;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GameRespTimestamp;
import com.gateway.gateway_api.games.data.classes.GameStatus;
import com.gateway.gateway_api.games.data.classes.GameStatusResp;
import com.gateway.gateway_api.games.data.classes.PublisherReqPatch;
import com.gateway.gateway_api.games.data.classes.PublisherReqPost;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.games.services.DevelopersService;
import com.gateway.gateway_api.games.services.GamesRequesterService;
import com.gateway.gateway_api.games.services.GamesService;
import com.gateway.gateway_api.games.services.PublishersService;
import com.gateway.gateway_api.games.services.UpdatableGameService;
import com.gateway.gateway_api.preferences.data.classes.CreatedGameResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.preferences.services.PreferencesService;
import com.gateway.gateway_api.reviews.data.classes.ReviewResp;
import com.gateway.gateway_api.reviews.services.ReviewsRequesterService;
import com.gateway.gateway_api.reviews.services.ReviewsService;

@Service
public class AdminService implements IAdminService {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
	
	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private CacheSynchronizationService cacheSynchronizationService;
	
	@Autowired
	private GameStatsCacheService gameStatsCacheService;
	
	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private ReviewsRequesterService reviewsRequesterService;
	
	@Autowired
	private PreferenceCacheService preferenceCacheService;
	
	@Autowired
	private UpdatableGameService updatableGameService;
	
	@Autowired
	private GamesService gamesService;
	
	@Autowired
	private PreferencesService preferencesService;
	
	@Autowired
	private ReviewsService reviewsService;
	
	@Autowired
	private DevelopersService developersService;
	
	@Autowired
	private PublishersService publishersService;

	@Override
	public CollectionResponse<Long> synchAllGamesWithReviews() {
		
		LocalTime start = appProperties.getSchedulerConfig().getStartingHour();
		// end equals start + 5 hours
		LocalTime end = start.plusHours(5);
		
		LocalTime now = LocalTime.now();
		
		// this service must be invoked only at a certain time interval
		if(now.isBefore(start) || now.isAfter(end)) {
			throw new UserReadableException("This service is not available at the current time.");
		}
		
		// retrieve all game ids
		CollectionResponse<Long> allIds = gamesRequesterService.findAllGameIds();
		
		// handle errors
		if (allIds.getContent().isEmpty() && !allIds.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(allIds);
		}
		
		if (allIds.getContent().isEmpty()) {
			throw new UserReadableException("No game ids retrieved. Check for assistance.", "204");
		}
		
		List<Long> gameIdsFailed = new ArrayList<>();
		List<Long> gameIdsSuccess = new ArrayList<>();
		List<Long> allIdsList = allIds.getContent().stream().toList();
		
		this.recalculateAndUpdate(allIdsList, gameIdsFailed, gameIdsSuccess);
		
		CollectionResponse<Long> response = new CollectionResponse<>(gameIdsSuccess);
		
		if(!gameIdsFailed.isEmpty()) {
			response.getMessages().add(new Message("Some games failed to update: " + gameIdsFailed,
					"500"));
		}
		
		
		return response;
	}
	
	@Override
	public CollectionResponse<Long> synchInputGamesWithReviews(List<Long> gameIds) {
		
		List<Long> gameIdsFailed = new ArrayList<>();
		List<Long> gameIdsSuccess = new ArrayList<>();
		
		this.recalculateAndUpdate(gameIds, gameIdsFailed, gameIdsSuccess);
		
		CollectionResponse<Long> response = new CollectionResponse<>(gameIdsSuccess);
		
		if(!gameIdsFailed.isEmpty()) {
			response.getMessages().add(new Message("Some games failed to update: " + gameIdsFailed,
					"500"));
		}
		
		return response;
	}
	
	private void recalculateAndUpdate(List<Long> gameIds, 
			List<Long> gameIdsFailed,
			List<Long> gameIdsSuccess) {
		for(Long gameId : gameIds) {
			boolean updateSuccessfull = cacheSynchronizationService.recalculateAndUpdate(gameId);
			
			if(!updateSuccessfull) {
				gameIdsFailed.add(gameId);
			} else {
				gameIdsSuccess.add(gameId);
				// wipe cache entry
				try {
					gameStatsCacheService.removeReviewChangeKey(gameId);
				} catch (DataAccessException ex) {
					logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
				}
			}
		}
	}
	
	@Override
	public CollectionResponse<GameResp> deleteAllGamesByIds(List<Long> gameIds) {
		
		CollectionResponse<GameResp> response = gamesRequesterService.deleteAllGamesByIds(gameIds);

		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// wipe cache entries
		gameIds.forEach(id -> {
			gamesService.handleCacheEviction(id); 
			updatableGameService.handleCacheEviction(id);
			// find the user that owns that game
			CollectionResponse<Long> userResp = preferencesRequesterService.findUserIdByOwnedGame(id);
			if(!userResp.getContent().isEmpty()) {
				Long userId = userResp.getContent().stream().findFirst().orElse(null);
				preferencesService.handleCacheEviction(userId);
				// clean also review cache
				reviewsService.handleCacheEviction(id, userId);
			}
		});
		
		// delete all references on preference db
		CollectionResponse<CreatedGameResp> createdGamesResp = preferencesRequesterService.deleteGamesWithIds(gameIds);
		
		// handle errors
		if (createdGamesResp.getContent().isEmpty() && !createdGamesResp.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(createdGamesResp);
		}
		
		
		// delete all reviews of those games
		CollectionResponse<ReviewResp> reviewsDeleted = reviewsRequesterService.deleteByGameIds(gameIds);
		
		// handle errors
		if (reviewsDeleted.getContent().isEmpty() && !reviewsDeleted.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(reviewsDeleted);
		}
		
        return response;
        
    }
	
	@Override
	public CollectionResponse<GameRespTimestamp> findAllGamesOnPending(Pageable pageable) {
		MultiValueMap<String, String> params = CustomUtils.convertParams(null, pageable);
		CollectionResponse<GameRespTimestamp> response = gamesRequesterService.findAllGamesOnPending(params);

		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
        return response;
        
    }
	
	@Override
	public CollectionResponse<GameStatusResp> patchStatus(List<GameReqPatchStatus> gameReqPatchStatusList) {
		
		CollectionResponse<GameStatusResp> response = gamesRequesterService.patchStatus(gameReqPatchStatusList);
		
		return response;
	}
	
	@Override
	public CollectionResponse<DeveloperResp> createDeveloper(DeveloperReqPost request) {
		
		CollectionResponse<DeveloperResp> response = gamesRequesterService.createDeveloper(request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}
	
	@Override
	public CollectionResponse<DeveloperResp> updateDeveloperById(Long id, DeveloperReqPatch request) {
		
		CollectionResponse<DeveloperResp> response = gamesRequesterService.updateDeveloperById(id, request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// wipe cache entry
		developersService.handleCacheEviction(id);
		
		return response;
	}
	
	@Override
	public CollectionResponse<PublisherResp> createPublisher(PublisherReqPost request) {
		
		CollectionResponse<PublisherResp> response = gamesRequesterService.createPublisher(request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}
	
	@Override
	public CollectionResponse<PublisherResp> updatePublisherById(Long id, PublisherReqPatch request) {
		
		CollectionResponse<PublisherResp> response = gamesRequesterService.updatePublisherById(id, request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// wipe cache entry
		publishersService.handleCacheEviction(id);
		
		return response;
	}
	
	@Override
	public CollectionResponse<GameResp> updateGame(Long id, GameReqPut request) {
		
		CollectionResponse<Long> userIdFound = preferencesRequesterService.findUserIdByOwnedGame(id);
		
		// handle errors
		if (userIdFound.getContent().isEmpty() && !userIdFound.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(userIdFound);
		}
		
		Long userId = userIdFound.getContent().stream().findFirst().orElse(null);
		
		// find the status of the game
		CollectionResponse<GameStatusResp> statusResp = gamesRequesterService.findGameStatus(id);
		
		if(!statusResp.getContent().isEmpty()) {
			GameStatusResp status = statusResp.getContent().stream().findFirst().orElse(null);
			if(status.getStatus().getCode() != GameStatus.APPROVED.getCode()) {
				throw new UserReadableException("Game must be approved first to be updated", "400");
			}
		}
		
		CollectionResponse<GameResp> response = gamesRequesterService.update(id, request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// update games cache
		gamesService.handleCacheEviction(id);
		
		// wipe value on preference cache if it exists.
		// this must be done to have new created games 'updated_at' timestamps
		try {
			preferenceCacheService.evictPreferenceId(userId);
		} catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
		
		GameResp gameUpdated = response.getContent().stream().findFirst().orElse(null);
		
		String customErrorMessage = "Game with id: " + gameUpdated.getId()
		+ " was updated successfully but there was a problem with updating the user preference. "
		+ "Check for assistance.";
		
		// update the preference
		CollectionResponse<CreatedGameResp> createdGameResp = preferencesRequesterService.updateUserCreatedGame(id, userId);
		
		// handle errors
		if (createdGameResp.getContent().isEmpty() && !createdGameResp.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(createdGameResp, customErrorMessage);
		}
		
		return response;
	}
}

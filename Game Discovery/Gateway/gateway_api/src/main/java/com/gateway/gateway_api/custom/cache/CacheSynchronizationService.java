package com.gateway.gateway_api.custom.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.security.JwtService;
import com.gateway.gateway_api.custom.security.TokenService;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.GameReqPatch;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.games.services.GamesRequesterService;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.reviews.data.classes.ReviewResp;
import com.gateway.gateway_api.reviews.data.classes.ReviewRespAvgRating;
import com.gateway.gateway_api.reviews.data.classes.ReviewRespCount;
import com.gateway.gateway_api.reviews.services.ReviewsRequesterService;

@Service
public class CacheSynchronizationService {

	private static final Logger logger = LoggerFactory.getLogger(CacheSynchronizationService.class);

	@Autowired
	private GameStatsCacheService gameStatsCacheService;

	@Autowired
	private ReviewsRequesterService reviewsRequesterService;

	@Autowired
	private GamesRequesterService gamesRequesterService;

	@Autowired
	private UserSynchService userSynchService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PreferencesRequesterService preferencesRequesterService;

	private static final Integer SYNCHRONIZE_LIMIT = 199; // 200*3 calls to db maximum every hour
	private static final Integer SYNCHRONIZE_CONCURRENT_GAMES_LIMIT = 5; // 6*3 calls to db maximum every 5 minutes
	private static final Integer SYNCHRONIZE_CONCURRENT_USER_LIMIT = 4; // 4*2 calls maximum

	private static final long MIN_UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(3);

	@Async
	@Scheduled(fixedRate = 60, timeUnit = TimeUnit.MINUTES) // Every 60 minutes
	public void synchronizeGameCacheWithDatabase() {
		logger.info("Scheduled game avgRating and reviewQuantity synchronization task initialized at: " + System.currentTimeMillis());
		// if cache is down there will be errors but are not be blocking the application
		Set<String> keys = new HashSet<>();
		try {
			keys = gameStatsCacheService.getKeysWithPrefix();
		} catch (DataAccessException ex) {
			logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}

		this.setupSecurityContextForAsyncThread();

		int i = 0;

		for (String key : keys) {
			Long gameId = Long.valueOf(key.replace("reviewScheduled:", ""));
			Integer reviewChangeCount = 0;
			try {
				reviewChangeCount = gameStatsCacheService.getReviewChangeCount(gameId);
			} catch (DataAccessException ex) {
				logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
			}

			if (reviewChangeCount > 0) {
				i += 1;
				boolean updateSuccessfull = recalculateAndUpdate(gameId);
				if (updateSuccessfull) {
					try {
						gameStatsCacheService.removeReviewChangeKey(gameId);
					} catch (DataAccessException ex) {
						logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
					}
				}

			}

			// limit the amount of calls to the DB
			if (i > SYNCHRONIZE_LIMIT) {
				break;
			}
		}
		logger.info("Scheduled game avgRating and reviewQuantity synchronization task finished at: " + System.currentTimeMillis());
	}

	@Async
	@Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES) // Every 5 minutes
	public void synchronizeGameCacheWithDatabaseUsingConcurrentMap() {
		long currentTime = System.currentTimeMillis();
		logger.info("Scheduled game avgRating and reviewQuantity synchronization using_concurrent_map task initialized at: " + currentTime);
		ConcurrentMap<Long, Long> gameIdMap = gameStatsCacheService.getGameIdMap();

		this.setupSecurityContextForAsyncThread();

		int i = 0;

		List<Long> gameIdsToRemove = new ArrayList<>();

		// this scheduler checks every 5 minutes if it can update any of the games
		// stored
		// on the concurrent map
		// it will update db only for those values that have not had any update since
		// long
		for (Long gameId : gameIdMap.keySet()) {
			// if game id doesn't exist on cache remove it from internal concurrent map
			// some other container may have it already updated
			String value = "";
			try {
				value = gameStatsCacheService.getReviewChangeCountOrNull(gameId);
			} catch (DataAccessException ex) {
				logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
			}
			if (value == null) {
				// key is not on cache, remove it from concurrent map
				// go on for next values
				// if cache is down for some reason value will be "" so we don't enter there
				gameIdsToRemove.add(gameId);
				continue;
			}

			Long lastUpdateTime = gameIdMap.get(gameId);
			if (currentTime - lastUpdateTime > MIN_UPDATE_INTERVAL) {
				i += 1;
				boolean updateSuccessfull = recalculateAndUpdate(gameId);
				if (updateSuccessfull) {
					gameIdsToRemove.add(gameId);
					try {
						gameStatsCacheService.removeReviewChangeKey(gameId);
					} catch (DataAccessException ex) {
						logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
					}
				}
			}

			// limit reached
			if (i > SYNCHRONIZE_CONCURRENT_GAMES_LIMIT) {
				break;
			}
		}

		for (Long gameId : gameIdsToRemove) {
			gameStatsCacheService.removeGameId(gameId);
		}

		logger.info(
				"Scheduled game avgRating and reviewQuantity synchronization using_concurrent_map task finished at: " + System.currentTimeMillis());
	}

	@Async
	@Scheduled(fixedRate = 20, timeUnit = TimeUnit.MINUTES) // Every 20 minutes
	public void synchronizeUserCacheWithDatabaseUsingConcurrentMap() {
		long currentTime = System.currentTimeMillis();
		logger.info("Scheduled user reviews and preferences deletion using_concurrent_map task initialized at: " + currentTime);
		ConcurrentMap<Long, Long> userIdMap = userSynchService.getUserToDeleteIdMap();

		this.setupSecurityContextForAsyncThread();

		int i = 0;

		Set<Long> keySet = userIdMap.keySet();
		Set<String> keySetString = new HashSet<>();

		List<Long> userIdsToRemove = new ArrayList<>();

		if (keySet.isEmpty()) {
			try {
				keySetString = userSynchService.getKeysWithPrefix();
			} catch (DataAccessException ex) {
				logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
			}

			for (String key : keySetString) {
				Long userId = Long.valueOf(key.replace("userToDelete:", ""));
				boolean deleteSuccessfull = deleteUserDetails(userId);
				if (deleteSuccessfull) {
					try {
						userSynchService.removeKeyFromCache(userId);
					} catch (DataAccessException ex) {
						logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
					}
				}
			}
		}

		// if the keySet is not empty we look on its values

		// this scheduler checks every 20 minutes if it can delete any of the user
		// preferences and reviews stored on the concurrent map
		for (Long userId : userIdMap.keySet()) {
			// if game id doesn't exist on cache remove it from internal concurrent map
			// some other container may have it already updated
			String value = "";
			try {
				value = userSynchService.getValue(userId);
			} catch (DataAccessException ex) {
				logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
			}

			if (value == null) {
				// value not found on cache remove it from internal storage also
				userIdsToRemove.add(userId);
				continue;
			}

			i += 1;
			boolean deleteSuccessfull = deleteUserDetails(userId);
			if (deleteSuccessfull) {
				userIdsToRemove.add(userId);
				try {
					userSynchService.removeKeyFromCache(userId);
				} catch (DataAccessException ex) {
					logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
				}
			}

			// limit reached
			if (i > SYNCHRONIZE_CONCURRENT_USER_LIMIT) {
				break;
			}
		}

		for (Long userId : userIdsToRemove) {
			userSynchService.removeUserToDeleteId(userId);
		}

		logger.info(
				"Scheduled user reviews and preferences deletion using_concurrent_map task finished at: " + System.currentTimeMillis());
	}

	private void setupSecurityContextForAsyncThread() {
		String jwt = tokenService.generateToken();

		UserDetails userDetails = jwtService.parseToken(jwt);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, jwt,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	public boolean deleteUserDetails(Long userId) {
		// delete reviews associated with the user
		CollectionResponse<ReviewResp> deleteReviewResp = reviewsRequesterService.deleteAll(userId);

		// handle errors
		if (deleteReviewResp.getContent().isEmpty() && !deleteReviewResp.getMessages().isEmpty()) {
			return false;
		}
		
		CollectionResponse<PreferenceResp> preferenceResp = preferencesRequesterService.findById(userId);
		
		// handle errors
		if (preferenceResp.getContent().isEmpty() && !preferenceResp.getMessages().isEmpty()) {
			return false;
		}
		
		if(!preferenceResp.getContent().isEmpty()) {
			// delete the developer and publisher the user may have created
			PreferenceResp preferenceFound = preferenceResp.getContent().stream().findFirst().orElse(null);
			
			Set<Long> developersIds = preferenceFound.getCreatedDeveloperIds();
			Set<Long> publishersIds = preferenceFound.getCreatedPublisherIds();
			
			if(!developersIds.isEmpty()) {
				Long developerId = developersIds.stream().findFirst().orElse(null);
				// delete the developerId
				CollectionResponse<DeveloperResp> result = gamesRequesterService.deleteDeveloperById(developerId);
				
				// handle errors
				if (result.getContent().isEmpty() && !result.getMessages().isEmpty()) {
					return false;
				}
			}
			
			if(!developersIds.isEmpty()) {
				Long publisherId = publishersIds.stream().findFirst().orElse(null);
				// delete the publisherId
				CollectionResponse<PublisherResp> result = gamesRequesterService.deletePublisherById(publisherId);
				
				// handle errors
				if (result.getContent().isEmpty() && !result.getMessages().isEmpty()) {
					return false;
				}
			}
		}

		// the second thing to delete is the user preference
		CollectionResponse<PreferenceResp> deletePreferenceResp = preferencesRequesterService.delete(userId);

		// handle errors
		if (deletePreferenceResp.getContent().isEmpty() && !deletePreferenceResp.getMessages().isEmpty()) {
			return false;
		}

		return true;
	}

	public boolean recalculateAndUpdate(Long gameId) {

		// recalculate the average rating
		CollectionResponse<ReviewRespAvgRating> avgRatingResp = reviewsRequesterService.getReviewAvgRating(gameId);

		if (CollectionUtils.isEmpty(avgRatingResp.getContent())) {
			return false;
		}

		ReviewRespAvgRating newAverage = avgRatingResp.getContent().stream().findFirst().orElse(null);

		// recalculate the review quantity
		CollectionResponse<ReviewRespCount> reviewQuantityResp = reviewsRequesterService.getReviewQuantity(gameId);

		if (CollectionUtils.isEmpty(reviewQuantityResp.getContent())) {
			return false;
		}

		ReviewRespCount newCount = reviewQuantityResp.getContent().stream().findFirst().orElse(null);

		// update the game db entry with the new values
		GameReqPatch patchRequest = new GameReqPatch();
		patchRequest.setReviewQuantity(newCount.getCount());
		patchRequest.setAverageRating(newAverage.getAverageRating());

		CollectionResponse<GameResp> gamePatchResp = gamesRequesterService.patchReviewQuantityAndAvgRating(gameId,
				patchRequest);

		// handle errors
		if (gamePatchResp == null || gamePatchResp.getContent().isEmpty()) {
			return false;
		}

		return true;
	}
}

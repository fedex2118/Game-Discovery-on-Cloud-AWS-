package com.gateway.gateway_api.reviews.services;

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
import com.gateway.gateway_api.custom.cache.GameStatsCacheService;
import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GameStatus;
import com.gateway.gateway_api.games.services.GamesRequesterService;
import com.gateway.gateway_api.reviews.data.classes.ReviewCriteria;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPost;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPut;
import com.gateway.gateway_api.reviews.data.classes.ReviewResp;
import com.gateway.gateway_api.reviews.data.classes.ReviewUpdateResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

@Service
public class ReviewsService implements IReviewsService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewsService.class);

	@Autowired
	private UsersRequesterService usersRequesterService;

	@Autowired
	private GamesRequesterService gamesRequesterService;

	@Autowired
	private ReviewsRequesterService reviewsRequesterService;

	@Autowired
	private GameStatsCacheService gameStatsCacheService;

	@Autowired
	private CacheManager cacheManager;

	@Override
	public CollectionResponse<ReviewResp> create(ReviewReqPost request) {

		// user must be authorized
		CustomUtils.authorize(request.getUserId(), usersRequesterService);

		// game must exist
		CollectionResponse<GameResp> gameResp = gamesRequesterService.findGameById(request.getGameId());

		if (gameResp.getContent().isEmpty()) {
			throw new UserReadableException("Game with id: " + request.getGameId() + " not found", "404");
		}
		
		// game must have approved status
		GameResp gameFound = gameResp.getContent().stream().findFirst().orElse(null);
		GameStatus status = gameFound.getStatus();
		
		if(status.getCode() != GameStatus.APPROVED.getCode()) {
			throw new UserReadableException("Cannot issue reviews for games without 'APPROVED' status", "400");
		}
		
		Long gameId = gameFound.getId();

		// create review
		CollectionResponse<ReviewResp> response = reviewsRequesterService.create(request);

		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		this.handleCacheEviction(gameId, request.getUserId());

		try {
			// update the scheduler cache
			gameStatsCacheService.incrementReviewChangeCount(gameId);
		} catch (DataAccessException ex) {
			// log exception and proceed without crashing
			logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}

		// update internal scheduler concurrent map
		gameStatsCacheService.addOrUpdateGameId(gameId);

		return response;
	}

	@Override
	public CollectionResponse<ReviewUpdateResp> update(Long gameId, Long userId, ReviewReqPut request) {

		// user must be authorized
		CustomUtils.authorize(userId, usersRequesterService);

		// game must exist
		CollectionResponse<GameResp> gameResp = gamesRequesterService.findGameById(gameId);

		if (gameResp.getContent().isEmpty()) {
			throw new UserReadableException("Game with id: " + gameId + " not found", "404");
		}
		
		// game must have approved status
		GameResp gameFound = gameResp.getContent().stream().findFirst().orElse(null);
		GameStatus status = gameFound.getStatus();
		
		if(status.getCode() != GameStatus.APPROVED.getCode()) {
			throw new UserReadableException("Cannot update reviews for games without 'APPROVED' status", "400");
		}
		
		// clean cache key before
		this.handleCacheEviction(gameId, userId);

		// update review
		CollectionResponse<ReviewUpdateResp> responseUpdate = reviewsRequesterService.update(gameId, userId, request);

		// handle errors
		if (responseUpdate.getContent().isEmpty() && !responseUpdate.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(responseUpdate);
		}

		try {
			// update the scheduler cache
			gameStatsCacheService.incrementReviewChangeCount(gameId);
		} catch (DataAccessException ex) {
			// log exception and proceed without crashing
			logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
		
		// update internal scheduler concurrent map
		gameStatsCacheService.addOrUpdateGameId(gameId);

		return responseUpdate;
	}

	@Override
	public CollectionResponse<ReviewResp> findByKey(Long gameId, Long userId) {

		// users can find reviews made by other users
		// without any restriction
		
		CollectionResponse<ReviewResp> cacheResponse = this.handleCacheResponse(gameId,
				userId);
		
		if(cacheResponse != null) {
			return cacheResponse;
		}

		CollectionResponse<ReviewResp> response = reviewsRequesterService.findByKey(gameId, userId);

		this.handleCacheInsertion(gameId, userId,  response);
		
		return response;
	}

	@Override
	public CollectionResponse<ReviewResp> findAllByUserId(Long userId, Pageable pageable) {

		// users can find reviews made by other users
		// without any restriction

		MultiValueMap<String, String> params = CustomUtils.convertParams(null, pageable);

		CollectionResponse<ReviewResp> response = reviewsRequesterService.findAllByUserId(userId, params);

		return response;
	}

	@Override
	public CollectionResponse<ReviewResp> findByCriteria(ReviewCriteria criteria, Pageable pageable) {

		MultiValueMap<String, String> params = CustomUtils.convertParams(criteria, pageable);

		CollectionResponse<ReviewResp> response = reviewsRequesterService.findByCriteria(params);

		return response;

	}

	@Override
	public CollectionResponse<ReviewResp> deleteByKey(Long gameId, Long userId) {

		// user must be authorized
		CustomUtils.authorize(userId, usersRequesterService);

		// game must exist
		CollectionResponse<GameResp> gameResp = gamesRequesterService.findGameById(gameId);

		if (gameResp.getContent().isEmpty()) {
			throw new UserReadableException("Game with id: " + gameId + " not found", "404");
		}
		
		// game must have approved status
		GameResp gameFound = gameResp.getContent().stream().findFirst().orElse(null);
		GameStatus status = gameFound.getStatus();
		
		if(status.getCode() != GameStatus.APPROVED.getCode()) {
			throw new UserReadableException("Cannot delete reviews for games without 'APPROVED' status", "400");
		}

		// find the existing review or throw error
		CollectionResponse<ReviewResp> reviewFoundResp = reviewsRequesterService.findByKey(gameId, userId);

		if (reviewFoundResp.getContent().isEmpty()) {
			throw new UserReadableException("Review with game id: " + gameId + " and userId: " + userId + " not found.",
					"404");
		}

		// delete review
		CollectionResponse<ReviewResp> response = reviewsRequesterService.delete(gameId, userId);

		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// clean cache key
		this.handleCacheEviction(gameId, userId);

		try {
			// update the scheduler cache
			gameStatsCacheService.incrementReviewChangeCount(gameId);
		} catch (DataAccessException ex) {
			// log exception and proceed without crashing
			logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
		
		// update internal scheduler concurrent map
		gameStatsCacheService.addOrUpdateGameId(gameId);

		return response;
	}

	private CollectionResponse<ReviewResp> handleCacheResponse(Long gameId, Long userId) {
		try {
			// Perform cache eviction or other Redis operations here
			String key = gameId + "_" + userId;
			ValueWrapper valueWrapper = cacheManager.getCache("reviewCache").get(key);
			if (valueWrapper != null) {
				Object value = valueWrapper.get();
				if (value instanceof CollectionResponse) {
					return (CollectionResponse<ReviewResp>) value;
				}
			}
		} catch (DataAccessException ex) {
			// log exception and proceed without crashing
			logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
		return null;
	}

	private void handleCacheInsertion(Long gameId, Long userId, CollectionResponse<ReviewResp> responseDB) {
		try {
			// Perform cache eviction or other Redis operations here
			String key = gameId + "_" + userId;
			cacheManager.getCache("reviewCache").putIfAbsent(key, responseDB);
		} catch (DataAccessException ex) {
			// Log the exception and proceed without crashing
			logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
	}

	public void handleCacheEviction(Long gameId, Long userId) {
		try {
			// Perform cache eviction or other Redis operations here
			String key = gameId + "_" + userId;
			cacheManager.getCache("reviewCache").evict(key);
		} catch (DataAccessException ex) {
			// Log the exception and proceed without crashing
			logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}
	}

}

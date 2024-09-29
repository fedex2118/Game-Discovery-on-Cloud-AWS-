package com.gateway.gateway_api.games.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPost;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;
import com.gateway.gateway_api.games.data.classes.UpdatableGameResp;
import com.gateway.gateway_api.preferences.data.classes.CreatedGameResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.users.data.classes.UserResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

@Service
public class UpdatableGameService implements IUpdatableGameService {
	
	private static final Logger logger = LoggerFactory.getLogger(UpdatableGameService.class);
	
	@Autowired
	private UsersRequesterService usersRequesterService;
	
	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	@Autowired
	private CacheManager cacheManager;
    
	@Override
	public CollectionResponse<UpdatableGameResp> findById(Long id) {
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// find out if the game is owned by the user
		CollectionResponse<CreatedGameResp> createdGameResp = preferencesRequesterService.findCreatedGameByKey(id, userRespId.getId());

		if(createdGameResp.getContent().isEmpty()) {
			return new CollectionResponse<>();
		}
		
		// if value is present on cache return that one
		CollectionResponse<UpdatableGameResp> cacheResponse = this.handleCacheResponse(id);
		
		if(cacheResponse != null) {
			return cacheResponse;
		}
		
		// find the updatable game
		CollectionResponse<UpdatableGameResp> response = gamesRequesterService.findUpdatableGameById(id);
		
		// update cache
		this.handleCacheInsertion(id, response);
		
		return response;
	}
	
	@Override
	public CollectionResponse<UpdatableGameResp> findAll(Pageable pageable) {
		
		MultiValueMap<String, String> params = CustomUtils.convertParams(null, pageable);
        
		CollectionResponse<UpdatableGameResp> response = gamesRequesterService.findAllUpdatableGames(params);
		
		return response;
    }
	
	@Override
	public CollectionResponse<UpdatableGameResp> create(UpdatableGameReqPost request) {
		
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// find out if the game is owned by the user
		CollectionResponse<CreatedGameResp> createdGameResp = preferencesRequesterService.findCreatedGameByKey(request.getId(), userRespId.getId());

		if(createdGameResp.getContent().isEmpty()) {
			throw new UserReadableException("No game found", "404");
		}
		
		CollectionResponse<UpdatableGameResp> response = gamesRequesterService.createUpdatableGame(request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		this.handleCacheEviction(request.getId());
		
		return response;
	}
	
	@Override
	@Transactional
	public CollectionResponse<UpdatableGameResp> update(Long id, UpdatableGameReqPut request) {
		
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// find out if the game is owned by the user
		CollectionResponse<CreatedGameResp> createdGameResp = preferencesRequesterService.findCreatedGameByKey(id, userRespId.getId());
		
		if(createdGameResp.getContent().isEmpty()) {
			throw new UserReadableException("No game found", "404");
		}
		
		CollectionResponse<UpdatableGameResp> response = gamesRequesterService.updateUpdatableGame(id, request);

		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// evict from cache
		this.handleCacheEviction(id);
		
		return response;
	}
	
	@Override
	@Transactional
	public CollectionResponse<UpdatableGameResp> deleteById(Long id) {
		
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// find out if the game is owned by the user
		CollectionResponse<CreatedGameResp> createdGameResp = preferencesRequesterService.findCreatedGameByKey(id, userRespId.getId());
		
		if(createdGameResp.getContent().isEmpty()) {
			throw new UserReadableException("No game found", "404");
		}
		
		// delete updatable game
		CollectionResponse<UpdatableGameResp> response = gamesRequesterService.deleteUpdatableGameById(id);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// evict from cache
		this.handleCacheEviction(id);
		
		return response;
	}
	
	
	@Override
	@Transactional
	public CollectionResponse<UpdatableGameResp> deleteAllByIds(List<Long> ids) {
		
		// delete updatable games
		CollectionResponse<UpdatableGameResp> response = gamesRequesterService.deleteAllUpdatableGamesByIds(ids);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		List<UpdatableGameResp> responseList = response.getContent().stream().toList();
		
		// evict keys from cache
		if(!responseList.isEmpty()) {
			responseList.forEach(el -> this.handleCacheEviction(el.getId()));
		}
		
		return response;
	}
	
    private CollectionResponse<UpdatableGameResp> handleCacheResponse(Long gameId) {
        try {
        	 ValueWrapper valueWrapper =  cacheManager.getCache("updatableGameCache").get(gameId);
        	 if(valueWrapper != null) {
        		 Object value = valueWrapper.get();
        		 if(value instanceof CollectionResponse) {
        			 return (CollectionResponse<UpdatableGameResp>) value;
        		 }
        	 }
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
        return null;
    }
    
    private void handleCacheInsertion(Long gameId, CollectionResponse<UpdatableGameResp> responseDB) {
        try {
        	 cacheManager.getCache("updatableGameCache").putIfAbsent(gameId, responseDB);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
	
    public void handleCacheEviction(Long gameId) {
        try {
        	cacheManager.getCache("updatableGameCache").evict(gameId);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
}

package com.gateway.gateway_api.games.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPatch;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPost;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.preferences.data.classes.CreatedDeveloperReqPost;
import com.gateway.gateway_api.preferences.data.classes.CreatedDeveloperResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.users.data.classes.UserResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

@Service
public class DevelopersService implements IDevelopersService {
	
	private static final Logger logger = LoggerFactory.getLogger(DevelopersService.class);
	
	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private UsersRequesterService usersRequesterService;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	public CollectionResponse<DeveloperResp> create(DeveloperReqPost request) {
		
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// users may assign one developer to themselves
		// find if the user has already created a developer before
		CollectionResponse<CreatedDeveloperResp> createdDevelopersFoundResp = preferencesRequesterService.findCreatedDeveloperByUserId(userRespId.getId());
		
		if(!createdDevelopersFoundResp.getContent().isEmpty()) {
			throw new UserReadableException("You reached the maximum of developers you can create for this user.", "403");
		}
		
		// if the user didn't create any developers yet
		// create a new developer
		CollectionResponse<DeveloperResp> response = gamesRequesterService.createDeveloper(request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		DeveloperResp developerCreated = response.getContent().stream().findFirst().orElse(null);
		
		this.handleCacheEviction(developerCreated.getId());
		
		// create a reference on the user preferences db
		CreatedDeveloperReqPost developerPreferenceToCreate = new CreatedDeveloperReqPost();
		developerPreferenceToCreate.setDeveloperId(developerCreated.getId());
		developerPreferenceToCreate.setUserId(userRespId.getId());
		CollectionResponse<CreatedDeveloperResp> userDeveloperCreated = preferencesRequesterService.createUserDeveloper(developerPreferenceToCreate);
		
		// handle errors
		if (userDeveloperCreated.getContent().isEmpty() && !userDeveloperCreated.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(userDeveloperCreated, "The developer was created successfully with ID: " 
					+ developerCreated.getId() + " but it wasn't created under the user preferences for user with ID: " + userRespId.getId());
		}
		
		
		return response;
	}
	
	@Override
	public CollectionResponse<DeveloperResp> updateById(Long id, DeveloperReqPatch request) {
		
		// the user must be authorized to perform this action
		// find the user developer created by user id and user created developer id
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		CollectionResponse<CreatedDeveloperResp> createdUserDeveloper = preferencesRequesterService.getCreatedDeveloper(id, userRespId.getId());
		
		// if no result is found throw error
		if(!createdUserDeveloper.getContent().isEmpty()) {
			throw new UserReadableException("No developer with ID: " + id + "for user with ID: " + userRespId.getId(), "404");
		}
		
		CollectionResponse<DeveloperResp> response = gamesRequesterService.updateDeveloperById(id, request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// wipe entry from cache
		this.handleCacheEviction(id);
		
		return response;
	}
	
	@Override
	public CollectionResponse<DeveloperResp> findById(Long id) {
		
		CollectionResponse<DeveloperResp> cacheResponse = this.handleCacheResponse(id);
		
		if(cacheResponse != null) {
			return cacheResponse;
		}
		
		CollectionResponse<DeveloperResp> response = gamesRequesterService.findDeveloperById(id);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// create cache entry
		this.handleCacheInsertion(id, response);
		
		return response;
	}
	
	@Override
	public CollectionResponse<DeveloperResp> findByName(String name) {
		
		CollectionResponse<DeveloperResp> response = gamesRequesterService.findDeveloperByName(name);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}
	
    private CollectionResponse<DeveloperResp> handleCacheResponse(Long developerId) {
        try {
            // Perform cache eviction or other Redis operations here
        	 ValueWrapper valueWrapper =  cacheManager.getCache("developerCache").get(developerId);
        	 if(valueWrapper != null) {
        		 Object value = valueWrapper.get();
        		 if(value instanceof CollectionResponse) {
        			 return (CollectionResponse<DeveloperResp>) value;
        		 }
        	 }
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
        return null;
    }
    
    public void handleCacheInsertion(Long developerId, CollectionResponse<DeveloperResp> responseDB) {
        try {
            // Perform cache eviction or other Redis operations here
        	 cacheManager.getCache("developerCache").putIfAbsent(developerId, responseDB);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
	
    public void handleCacheEviction(Long developerId) {
        try {
            // Perform cache eviction or other Redis operations here
        	cacheManager.getCache("developerCache").evict(developerId);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
}

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
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.PublisherReqPatch;
import com.gateway.gateway_api.games.data.classes.PublisherReqPost;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.preferences.data.classes.CreatedPublisherReqPost;
import com.gateway.gateway_api.preferences.data.classes.CreatedPublisherResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.users.data.classes.UserResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

@Service
public class PublishersService implements IPublishersService {
	
	private static final Logger logger = LoggerFactory.getLogger(PublishersService.class);

	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private UsersRequesterService usersRequesterService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public CollectionResponse<PublisherResp> create(PublisherReqPost request) {
		
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// users may assign one publisher to themselves
		// find if the user has already created a publisher before
		CollectionResponse<CreatedPublisherResp> createdPublishersFoundResp = preferencesRequesterService.findCreatedPublisherByUserId(userRespId.getId());
		
		if(!createdPublishersFoundResp.getContent().isEmpty()) {
			throw new UserReadableException("You reached the maximum of publishers you can create for this user.", "403");
		}
		
		// if the user didn't create any publishers yet
		// create a new publisher
		CollectionResponse<PublisherResp> response = gamesRequesterService.createPublisher(request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		PublisherResp publisherCreated = response.getContent().stream().findFirst().orElse(null);
		
		this.handleCacheEviction(publisherCreated.getId());
		
		// create a reference on the user preferences db
		CreatedPublisherReqPost publisherPreferenceToCreate = new CreatedPublisherReqPost();
		publisherPreferenceToCreate.setPublisherId(publisherCreated.getId());
		publisherPreferenceToCreate.setUserId(userRespId.getId());
		CollectionResponse<CreatedPublisherResp> userDeveloperCreated = preferencesRequesterService.createUserPublisher(publisherPreferenceToCreate);
		
		// handle errors
		if (userDeveloperCreated.getContent().isEmpty() && !userDeveloperCreated.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(userDeveloperCreated, "The publisher was created successfully with ID: " 
					+ publisherCreated.getId() + " but it wasn't created under the user preferences for user with ID: " + userRespId.getId());
		}
		
		return response;
	}
	
	@Override
	public CollectionResponse<PublisherResp> updateById(Long id, PublisherReqPatch request) {
		
		// the user must be authorized to perform this action
		// find the user publisher created by user id and user created publisher id
		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		CollectionResponse<CreatedPublisherResp> createdUserDeveloper = preferencesRequesterService.getCreatedPublisher(id, userRespId.getId());
		
		// if no result is found throw error
		if(!createdUserDeveloper.getContent().isEmpty()) {
			throw new UserReadableException("No publisher with ID: " + id + " found for user with ID: " + userRespId.getId(), "404");
		}
		
		CollectionResponse<PublisherResp> response = gamesRequesterService.updatePublisherById(id, request);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// wipe cache entry
		this.handleCacheEviction(id);
		
		return response;
	}
	
	@Override
	public CollectionResponse<PublisherResp> findById(Long id) {
		
		CollectionResponse<PublisherResp> cacheResponse = this.handleCacheResponse(id);
		
		if(cacheResponse != null) {
			return cacheResponse;
		}
		
		CollectionResponse<PublisherResp> response = gamesRequesterService.findPublisherById(id);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// create cache entry
		this.handleCacheInsertion(id, response);
		
		return response;
	}
	
	@Override
	public CollectionResponse<PublisherResp> findByName(String name) {
		
		CollectionResponse<PublisherResp> response = gamesRequesterService.findPublisherByName(name);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}
	
    private CollectionResponse<PublisherResp> handleCacheResponse(Long publisherId) {
        try {
            // Perform cache eviction or other Redis operations here
        	 ValueWrapper valueWrapper =  cacheManager.getCache("publisherCache").get(publisherId);
        	 if(valueWrapper != null) {
        		 Object value = valueWrapper.get();
        		 if(value instanceof CollectionResponse) {
        			 return (CollectionResponse<PublisherResp>) value;
        		 }
        	 }
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
        return null;
    }
    
    public void handleCacheInsertion(Long publisherId, CollectionResponse<PublisherResp> responseDB) {
        try {
            // Perform cache eviction or other Redis operations here
        	 cacheManager.getCache("publisherCache").putIfAbsent(publisherId, responseDB);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
	
    public void handleCacheEviction(Long publisherId) {
        try {
            // Perform cache eviction or other Redis operations here
        	cacheManager.getCache("publisherCache").evict(publisherId);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
}

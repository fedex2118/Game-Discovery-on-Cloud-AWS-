package com.gateway.gateway_api.preferences.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GenreResp;
import com.gateway.gateway_api.games.data.classes.PlatformResp;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.games.services.GamesRequesterService;
import com.gateway.gateway_api.preferences.data.classes.PreferenceReqPost;
import com.gateway.gateway_api.preferences.data.classes.PreferenceReqPut;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;
import com.gateway.gateway_api.preferences.mapper.PreferencesMapper;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPost;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPut;
import com.gateway.gateway_api.users.services.UsersRequesterService;

@Service
public class PreferencesService implements IPreferencesService {

	private static final Logger logger = LoggerFactory.getLogger(PreferencesService.class);

	private PreferencesMapper preferencesMapper = Mappers.getMapper(PreferencesMapper.class);

	@Autowired
	private PreferencesRequesterService preferencesRequesterService;

	@Autowired
	private UsersRequesterService usersRequesterService;

	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	public CollectionResponse<PreferenceResp> findById(Long id) {
		
		// users can see other users preferences without worries
		
		CollectionResponse<PreferenceResp> cacheResponse = this.handleCacheResponse(id);
		
		if(cacheResponse != null) {
			return cacheResponse;
		}
		
		CollectionResponse<PreferenceResp> response = preferencesRequesterService.findById(id);

		this.handleCacheInsertion(id, response);
		
		return response;
	}

	@Override
	public CollectionResponse<PreferenceResp> create(Long id, PreferencesPost request) {

		// check if user is authorized
		CustomUtils.authorize(id, usersRequesterService);

		// DEVELOPERS
		Set<Long> developerIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getDeveloperIds())) {
			developerIdsFound = this.findDeveloperIds(request.getDeveloperIds(), developerIdsFound);
			request.setDeveloperIds(developerIdsFound);
		}

		// GENRES
		Set<Long> genreIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getGenreIds())) {
			genreIdsFound = this.findGenreIds(request.getGenreIds(), genreIdsFound);
			request.setGenreIds(genreIdsFound);
		}

		// PLATFORMS
		Set<Long> platformIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getPlatformIds())) {
			platformIdsFound = this.findPlatformIds(request.getPlatformIds(), platformIdsFound);
			request.setPlatformIds(platformIdsFound);
		}

		// PUBLISHERS
		Set<Long> publisherIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getPublisherIds())) {
			publisherIdsFound = this.findPublisherIds(request.getPublisherIds(), publisherIdsFound);
			request.setPublisherIds(publisherIdsFound);
		}

		// map the properties for the service
		PreferenceReqPost preferencePostRequest = preferencesMapper.toPreferenceReqPost(request);
		// add the id
		preferencePostRequest.setId(id);

		// create the new preference
		CollectionResponse<PreferenceResp> response = preferencesRequesterService.create(preferencePostRequest);

		this.handleCacheEviction(id);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}

		return response;
	}

	private Set<Long> findDeveloperIds(Collection<Long> developerIds, Set<Long> developerIdsFound) {
		for (Long id : developerIds) {
			CollectionResponse<DeveloperResp> devResp = gamesRequesterService.findDeveloperById(id);
			DeveloperResp devFound = devResp.getContent().stream().findFirst().orElse(null);
			if (devFound != null) {
				developerIdsFound.add(devFound.getId());
			}
		}
		if (developerIdsFound.isEmpty()) {
			developerIdsFound = null;
		}
		return developerIdsFound;
	}

	private Set<Long> findGenreIds(Collection<Long> genreIds, Set<Long> genreIdsFound) {
		for (Long id : genreIds) {
			CollectionResponse<GenreResp> genreResp = gamesRequesterService.findGenreById(id);
			GenreResp genreFound = genreResp.getContent().stream().findFirst().orElse(null);
			if (genreFound != null) {
				genreIdsFound.add(genreFound.getId());
			}
		}
		if (genreIdsFound.isEmpty()) {
			genreIdsFound = null;
		}
		return genreIdsFound;
	}

	private Set<Long> findPlatformIds(Collection<Long> platformIds, Set<Long> platformIdsFound) {
		for (Long id : platformIds) {
			CollectionResponse<PlatformResp> platformResp = gamesRequesterService.findPlatformById(id);
			PlatformResp platformFound = platformResp.getContent().stream().findFirst().orElse(null);
			if (platformFound != null) {
				platformIdsFound.add(platformFound.getId());
			}
		}
		if (platformIdsFound.isEmpty()) {
			platformIdsFound = null;
		}
		return platformIdsFound;
	}

	private Set<Long> findPublisherIds(Collection<Long> publisherIds, Set<Long> publisherIdsFound) {
		for (Long id : publisherIds) {
			CollectionResponse<PublisherResp> publisherResp = gamesRequesterService.findPublisherById(id);
			PublisherResp publisherFound = publisherResp.getContent().stream().findFirst().orElse(null);
			if (publisherFound != null) {
				publisherIdsFound.add(publisherFound.getId());
			}
		}
		if (publisherIdsFound.isEmpty()) {
			publisherIdsFound = null;
		}
		return publisherIdsFound;
	}

	private Set<Long> findGameIds(Collection<Long> gameIds, Set<Long> gameIdsFound) {
		for (Long id : gameIds) {
			CollectionResponse<GameResp> gameResp = gamesRequesterService.findGameById(id);
			GameResp gameFound = gameResp.getContent().stream().findFirst().orElse(null);
			if (gameFound != null) {
				gameIdsFound.add(gameFound.getId());
			}
		}
		if (gameIdsFound.isEmpty()) {
			gameIdsFound = null;
		}
		return gameIdsFound;
	}

	@Override
	public CollectionResponse<PreferenceResp> update(Long id, PreferencesPut request) {

		// check if user is authorized
		CustomUtils.authorize(id, usersRequesterService);
		
		// clean cache key before
		this.handleCacheEviction(id);

		// DEVELOPERS
		Set<Long> developerIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getDeveloperIds())) {
			developerIdsFound = this.findDeveloperIds(request.getDeveloperIds(), developerIdsFound);
			request.setDeveloperIds(developerIdsFound);
		}

		// GENRES
		Set<Long> genreIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getGenreIds())) {
			genreIdsFound = this.findGenreIds(request.getGenreIds(), genreIdsFound);
			request.setGenreIds(genreIdsFound);
		}

		// PLATFORMS
		Set<Long> platformIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getPlatformIds())) {
			platformIdsFound = this.findPlatformIds(request.getPlatformIds(), platformIdsFound);
			request.setPlatformIds(platformIdsFound);
		}

		// PUBLISHERS
		Set<Long> publisherIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getPublisherIds())) {
			publisherIdsFound = this.findPublisherIds(request.getPublisherIds(), publisherIdsFound);
			request.setPublisherIds(publisherIdsFound);
		}

		// WISHLIST
		Set<Long> gameIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getWishlistIds())) {
			gameIdsFound = this.findGameIds(request.getWishlistIds(), gameIdsFound);
			request.setWishlistIds(gameIdsFound);
		}

		// LIBRARY
		gameIdsFound = new HashSet<>();
		if (!CollectionUtils.isEmpty(request.getLibraryIds())) {
			gameIdsFound = this.findGameIds(request.getLibraryIds(), gameIdsFound);
			request.setLibraryIds(gameIdsFound);
		}

		// map the properties for the service
		PreferenceReqPut preferencePutRequest = preferencesMapper.toPreferenceReqPut(request);

		// update the preference
		CollectionResponse<PreferenceResp> response = preferencesRequesterService.update(id, preferencePutRequest);

		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}

	@Override
	public CollectionResponse<PreferenceResp> deleteById(Long id) {
		
		// check if user is authorized
		CustomUtils.authorize(id, usersRequesterService);
		
		// update the preference
		CollectionResponse<PreferenceResp> response = preferencesRequesterService.delete(id);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// clean cache key
		this.handleCacheEviction(id);

		return response;
	}
	
    private CollectionResponse<PreferenceResp> handleCacheResponse(Long userId) {
        try {
            // Perform cache eviction or other Redis operations here
        	 ValueWrapper valueWrapper =  cacheManager.getCache("preferenceCache").get(userId);
        	 if(valueWrapper != null) {
        		 Object value = valueWrapper.get();
        		 if(value instanceof CollectionResponse) {
        			 return (CollectionResponse<PreferenceResp>) value;
        		 }
        	 }
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
        return null;
    }
    
    private void handleCacheInsertion(Long userId, CollectionResponse<PreferenceResp> responseDB) {
        try {
            // Perform cache eviction or other Redis operations here
        	 cacheManager.getCache("preferenceCache").putIfAbsent(userId, responseDB);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }
	
    public void handleCacheEviction(Long userId) {
        try {
            // Perform cache eviction or other Redis operations here
        	cacheManager.getCache("preferenceCache").evict(userId);
        } catch (DataAccessException ex) {
            // Log the exception and proceed without crashing
        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
        }
    }

}

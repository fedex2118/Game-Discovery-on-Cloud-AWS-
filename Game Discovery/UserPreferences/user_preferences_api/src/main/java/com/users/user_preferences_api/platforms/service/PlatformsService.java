package com.users.user_preferences_api.platforms.service;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.platforms.dto.PlatformReqPost;
import com.users.user_preferences_api.platforms.dto.PlatformResp;
import com.users.user_preferences_api.platforms.entity.PlatformEntity;
import com.users.user_preferences_api.platforms.entity.PlatformEntityKey;
import com.users.user_preferences_api.platforms.mapper.PlatformsMapper;
import com.users.user_preferences_api.platforms.repository.PlatformsRepository;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class PlatformsService implements IPlatformsService {
	
	private static final Logger logger = LoggerFactory.getLogger(PlatformsService.class);
	
	@Autowired
	private PlatformsRepository platformsRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private PlatformsMapper platformsMapper = Mappers.getMapper(PlatformsMapper.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public CollectionResponse<PlatformResp> create(PlatformReqPost platformReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(platformReqPost.getUserId())
				.orElseThrow();
		
		// save the entity
		PlatformEntity platformEntity = new PlatformEntity();
		
		PlatformEntityKey key = new PlatformEntityKey();
		key.setPlatformId(platformReqPost.getPlatformId());
		key.setUserId(platformReqPost.getUserId());
		
		platformEntity.setId(key);
		platformEntity.setUserPreference(userPreference);
		
		platformEntity = platformsRepository.save(platformEntity);
		
		PlatformResp response = new PlatformResp();
		response.setUserId(platformEntity.getId().getUserId());
		response.setPlatformId(platformEntity.getId().getPlatformId());
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "platformsCache", key = "#platformId + '_' + #userId" )
	public CollectionResponse<PlatformResp> findByKey(Long platformId, Long userId) {
		PlatformEntityKey key = new PlatformEntityKey();
		key.setPlatformId(platformId);
		key.setUserId(userId);
		
		PlatformEntity platformFound = platformsRepository.findById(key).orElse(null);
		
		if(platformFound != null) {
			logger.info(LoggerConstants.PLATFORM_ID_FOUND, platformFound.getId());
			PlatformResp response = platformsMapper.toPlatformResponse(platformFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<PlatformResp> findAllByUserId(Long userId, Pageable pageable) {
		Page<PlatformEntity> platformsFound = platformsRepository.findAllByIdUserId(userId, pageable);
		
		if(platformsFound != null) {
			//logger.info(LoggerConstants._ID_FOUND, Found.getId());
			List<PlatformResp> response = platformsMapper.toPlatformsResponse(platformsFound.getContent());
			
	        // convert it for pagination
	        Page<PlatformResp> responsePage = new PageImpl<>(response, pageable, platformsFound.getTotalElements());
			return new CollectionResponse<>(responsePage);
		}
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "platformsCache", key = "#platformId + '_' + #userId")
	public CollectionResponse<PlatformResp> deleteByKey(Long platformId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		PlatformEntityKey key = new PlatformEntityKey();
		key.setPlatformId(platformId);
		key.setUserId(userId);
		
		platformsRepository.deleteById(key);
		
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<PlatformResp> deleteUserPlatforms(Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		// first find all platforms for the input user
		List<PlatformEntity> platformsFound = platformsRepository.findAllByIdUserId(userId);
		
		// evict entries of that specific user
//		for (PlatformEntity platform : platformsFound) {
//	        cacheManager.getCache("platformsCache").evict(platform.getId() + "_" + userId);
//	    }
		
		// delete all found platform entries
		platformsRepository.deleteAll(platformsFound);
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<PlatformResp> deletePlatforms(Long platformId) {

	    // first find all platforms with input ID
	    List<PlatformEntity> platforms = platformsRepository.findAllByIdPlatformId(platformId);

	    // delete all found platform entries
	    if (!platforms.isEmpty()) {
			// evict entries of that specific user
//			for (PlatformEntity platform : platforms) {
//		        cacheManager.getCache("platformsCache").evict(platformId + "_" + platform.getId().getUserId());
//		    }
	    	
	    	platformsRepository.deleteAll(platforms);
	    }
	    
	    return new CollectionResponse<>();
	}
	
	

}

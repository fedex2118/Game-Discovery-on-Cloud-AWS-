package com.users.user_preferences_api.developers.service;

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
import com.users.user_preferences_api.developers.dto.DeveloperReqPost;
import com.users.user_preferences_api.developers.dto.DeveloperResp;
import com.users.user_preferences_api.developers.entity.DeveloperEntity;
import com.users.user_preferences_api.developers.entity.DeveloperEntityKey;
import com.users.user_preferences_api.developers.mapper.DevelopersMapper;
import com.users.user_preferences_api.developers.repository.DevelopersRepository;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class DevelopersService implements IDevelopersService {
	
	private static final Logger logger = LoggerFactory.getLogger(DevelopersService.class);
	
	@Autowired
	private DevelopersRepository developersRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private DevelopersMapper developersMapper = Mappers.getMapper(DevelopersMapper.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public CollectionResponse<DeveloperResp> create(DeveloperReqPost developerReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(developerReqPost.getUserId())
				.orElseThrow();
		
		// save the entity
		DeveloperEntity developerEntity = new DeveloperEntity();
		
		DeveloperEntityKey key = new DeveloperEntityKey();
		key.setDeveloperId(developerReqPost.getDeveloperId());
		key.setUserId(developerReqPost.getUserId());
		
		developerEntity.setId(key);
		developerEntity.setUserPreference(userPreference);
		
		developerEntity = developersRepository.save(developerEntity);
		
		DeveloperResp response = new DeveloperResp();
		response.setUserId(developerEntity.getId().getUserId());
		response.setDeveloperId(developerEntity.getId().getDeveloperId());
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "developersCache", key = "#developerId + '_' + #userId" )
	public CollectionResponse<DeveloperResp> findByKey(Long developerId, Long userId) {
		DeveloperEntityKey key = new DeveloperEntityKey();
		key.setDeveloperId(developerId);
		key.setUserId(userId);
		
		DeveloperEntity developerFound = developersRepository.findById(key).orElse(null);
		
		if(developerFound != null) {
			logger.info(LoggerConstants.DEVELOPER_ID_FOUND, developerFound.getId());
			DeveloperResp response = developersMapper.toDeveloperResponse(developerFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<DeveloperResp> findAllByUserId(Long userId, Pageable pageable) {
		Page<DeveloperEntity> developersFound = developersRepository.findAllByIdUserId(userId, pageable);
		
		if(developersFound != null) {
			//logger.info(LoggerConstants._ID_FOUND, Found.getId());
			List<DeveloperResp> response = developersMapper.toDevelopersResponse(developersFound.getContent());
			
	        // convert it for pagination
	        Page<DeveloperResp> responsePage = new PageImpl<>(response, pageable, developersFound.getTotalElements());
	        return new CollectionResponse<>(responsePage);
		}
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "developersCache", key = "#developerId + '_' + #userId")
	public CollectionResponse<DeveloperResp> deleteByKey(Long developerId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		DeveloperEntityKey key = new DeveloperEntityKey();
		key.setDeveloperId(developerId);
		key.setUserId(userId);
		
		developersRepository.deleteById(key);
		
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<DeveloperResp> deleteUserDevelopers(Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		// first find all developers for the input user
		List<DeveloperEntity> developersFound = developersRepository.findAllByIdUserId(userId);
		
		// evict entries of that specific user
//		for (DeveloperEntity developer : developersFound) {
//	        cacheManager.getCache("developersCache").evict(developer.getId().getDeveloperId() + "_" + userId);
//	    }
		
		// delete all found developer entries
		developersRepository.deleteAll(developersFound);
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<DeveloperResp> deleteDevelopers(Long developerId) {

	    // first find all developers with input ID
	    List<DeveloperEntity> developers = developersRepository.findAllByIdDeveloperId(developerId);

	    // delete all found developer entries
	    if (!developers.isEmpty()) {
			// evict entries of that specific user
//			for (DeveloperEntity developer : developers) {
//		        cacheManager.getCache("developersCache").evict(developerId + "_" + developer.getId().getUserId());
//		    }
	    	
	    	developersRepository.deleteAll(developers);
	    }
	    
	    return new CollectionResponse<>();
	}
	
}

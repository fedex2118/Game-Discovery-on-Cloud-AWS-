package com.users.user_preferences_api.publishers.service;

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
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.publishers.dto.PublisherReqPost;
import com.users.user_preferences_api.publishers.dto.PublisherResp;
import com.users.user_preferences_api.publishers.entity.PublisherEntity;
import com.users.user_preferences_api.publishers.entity.PublisherEntityKey;
import com.users.user_preferences_api.publishers.mapper.PublishersMapper;
import com.users.user_preferences_api.publishers.repository.PublishersRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class PublishersService implements IPublishersService {
	
	private static final Logger logger = LoggerFactory.getLogger(PublishersService.class);
	
	@Autowired
	private PublishersRepository publishersRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private PublishersMapper publishersMapper = Mappers.getMapper(PublishersMapper.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public CollectionResponse<PublisherResp> create(PublisherReqPost publisherReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(publisherReqPost.getUserId())
				.orElseThrow();
		
		// save the entity
		PublisherEntity publisherEntity = new PublisherEntity();
		
		PublisherEntityKey key = new PublisherEntityKey();
		key.setPublisherId(publisherReqPost.getPublisherId());
		key.setUserId(publisherReqPost.getUserId());
		
		publisherEntity.setId(key);
		publisherEntity.setUserPreference(userPreference);
		
		publisherEntity = publishersRepository.save(publisherEntity);
		
		PublisherResp response = new PublisherResp();
		response.setUserId(publisherEntity.getId().getUserId());
		response.setPublisherId(publisherEntity.getId().getPublisherId());
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "publishersCache", key = "#publisherId + '_' + #userId" )
	public CollectionResponse<PublisherResp> findByKey(Long publisherId, Long userId) {
		PublisherEntityKey key = new PublisherEntityKey();
		key.setPublisherId(publisherId);
		key.setUserId(userId);
		
		PublisherEntity publisherFound = publishersRepository.findById(key).orElse(null);
		
		if(publisherFound != null) {
			logger.info(LoggerConstants.PUBLISHER_ID_FOUND, publisherFound.getId());
			PublisherResp response = publishersMapper.toPublisherResponse(publisherFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<PublisherResp> findAllByUserId(Long userId, Pageable pageable) {
		Page<PublisherEntity> publishersFound = publishersRepository.findAllByIdUserId(userId, pageable);
		
		if(publishersFound != null) {
			//logger.info(LoggerConstants._ID_FOUND, Found.getId());
			List<PublisherResp> response = publishersMapper.toPublishersResponse(publishersFound.getContent());
			
	        // convert it for pagination
	        Page<PublisherResp> responsePage = new PageImpl<>(response, pageable, publishersFound.getTotalElements());
			return new CollectionResponse<>(responsePage);
		}
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "publishersCache", key = "#publisherId + '_' + #userId")
	public CollectionResponse<PublisherResp> deleteByKey(Long publisherId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		PublisherEntityKey key = new PublisherEntityKey();
		key.setPublisherId(publisherId);
		key.setUserId(userId);
		
		publishersRepository.deleteById(key);
		
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<PublisherResp> deleteUserPublishers(Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		// first find all publishers for the input user
		List<PublisherEntity> publishersFound = publishersRepository.findAllByIdUserId(userId);
		
		// evict entries of that specific user
//		for (PublisherEntity publisher : publishersFound) {
//	        cacheManager.getCache("publishersCache").evict(publisher.getId() + "_" + userId);
//	    }
		
		// delete all found publisher entries
		publishersRepository.deleteAll(publishersFound);
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<PublisherResp> deletePublishers(Long publisherId) {

	    // first find all publishers with input ID
	    List<PublisherEntity> publishers = publishersRepository.findAllByIdPublisherId(publisherId);

	    // delete all found publisher entries
	    if (!publishers.isEmpty()) {
			// evict entries of that specific user
//			for (PublisherEntity publisher : publishers) {
//		        cacheManager.getCache("publishersCache").evict(publisherId + "_" + publisher.getId().getUserId());
//		    }
	    	
	    	publishersRepository.deleteAll(publishers);
	    }
	    
	    return new CollectionResponse<>();
	}

}

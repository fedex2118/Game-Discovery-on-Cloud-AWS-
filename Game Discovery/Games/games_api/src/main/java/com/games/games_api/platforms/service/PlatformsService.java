package com.games.games_api.platforms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.platforms.dto.PlatformReqPatch;
import com.games.games_api.platforms.dto.PlatformReqPost;
import com.games.games_api.platforms.dto.PlatformResp;
import com.games.games_api.platforms.entity.PlatformEntity;
import com.games.games_api.platforms.mapper.PlatformsMapper;
import com.games.games_api.platforms.repository.PlatformsRepository;
import com.games.games_api.utils.LoggerConstants;

@Service
public class PlatformsService implements IPlatformsService {

	private static final Logger logger = LoggerFactory.getLogger(PlatformsService.class);

	@Autowired
	private PlatformsRepository platformsRepository;
	
	private PlatformsMapper platformsMapper = Mappers.getMapper(PlatformsMapper.class);

	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "platformsCache", key = "#id" )
	public CollectionResponse<PlatformResp> findById(Long id) {
		PlatformEntity platformEntity = platformsRepository.findById(id).orElse(null);

		if (platformEntity != null) {
			logger.info(LoggerConstants.PLATFORM_ID_FOUND, platformEntity);
			PlatformResp response = platformsMapper.toPlatformResponse(platformEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}

	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<PlatformResp> findByName(String name) {
		PlatformEntity platformEntity = platformsRepository.findByName(name).orElse(null);

		if (platformEntity != null) {
			logger.info(LoggerConstants.PLATFORM_ID_FOUND, platformEntity);
			PlatformResp response = platformsMapper.toPlatformResponse(platformEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<PlatformEntity> findAllById(Set<Long> ids) {
		// find genres
		Set<PlatformEntity> platforms = new HashSet<>();
		
		platforms.addAll(platformsRepository.findAllById(ids));
		
		return platforms;
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<PlatformResp> findAll(Pageable pageable) {
		Page<PlatformEntity> publishersFound =  platformsRepository.findAll(pageable);
		
		// prepare response
		List<PlatformResp> response = platformsMapper.toPlatformsResponse(publishersFound.getContent());
		
		// convert response for pagination
		Page<PlatformResp> responsePage = new PageImpl<>(response, pageable, publishersFound.getTotalElements()); 
		
		return new CollectionResponse<>(responsePage);
	}

	@Override
	public CollectionResponse<PlatformResp> create(PlatformReqPost platformReqPost) {
		PlatformEntity platformEntity = new PlatformEntity();
		platformEntity.setName(platformReqPost.getName());

		platformEntity = platformsRepository.save(platformEntity);

		PlatformResp response = platformsMapper.toPlatformResponse(platformEntity);
		logger.info(LoggerConstants.PLATFORM_CREATED, platformEntity);

		return new CollectionResponse<>(response);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "platformsCache", key = "#id")
	public CollectionResponse<PlatformResp> deleteById(Long id) {
		PlatformEntity platformEntity = platformsRepository.findById(id).orElseThrow();

		platformsRepository.deleteById(id);

		logger.info(LoggerConstants.PLATFORM_DELETED, platformEntity);

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "platformsCache", allEntries = true)
	public CollectionResponse<PlatformResp> deleteByName(String name) {
		PlatformEntity platformEntity = platformsRepository.findByName(name).orElseThrow();

		platformsRepository.deleteById(platformEntity.getId());;

		logger.info(LoggerConstants.PLATFORM_DELETED, platformEntity);

		return new CollectionResponse<>();
	}
	
	@Override
	//@CacheEvict(value = "platformsCache", key = "#id")
	public CollectionResponse<PlatformResp> updateById(Long id, PlatformReqPatch platformReqPatch) {
		PlatformEntity platformEntity = platformsRepository.findById(id).orElseThrow();
		platformEntity.setName(platformReqPatch.getName());
		platformEntity = platformsRepository.save(platformEntity);

		PlatformResp response = platformsMapper.toPlatformResponse(platformEntity);
		logger.info(LoggerConstants.PLATFORM_UPDATED, platformEntity);

		return new CollectionResponse<>(response);
	}
}

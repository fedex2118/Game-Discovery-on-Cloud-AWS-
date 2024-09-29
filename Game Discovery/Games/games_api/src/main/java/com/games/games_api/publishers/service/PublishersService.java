package com.games.games_api.publishers.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.games.specifications.GameSpecifications;
import com.games.games_api.publishers.dto.PublisherReqPatch;
import com.games.games_api.publishers.dto.PublisherReqPost;
import com.games.games_api.publishers.dto.PublisherResp;
import com.games.games_api.publishers.entity.PublisherEntity;
import com.games.games_api.publishers.mapper.PublishersMapper;
import com.games.games_api.publishers.repository.PublishersRepository;
import com.games.games_api.utils.LoggerConstants;

@Service
public class PublishersService implements IPublishersService {
	
	private static final Logger logger = LoggerFactory.getLogger(PublishersService.class);
	
	private PublishersMapper publishersMapper = Mappers.getMapper(PublishersMapper.class);

	@Autowired
	private PublishersRepository publishersRepository;
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "publishersCache", key = "#id" )
	public CollectionResponse<PublisherResp> findById(Long id) {
		PublisherEntity publisherEntity = publishersRepository.findById(id).orElse(null);

		if (publisherEntity != null) {
			logger.info(LoggerConstants.PUBLISHER_ID_FOUND, publisherEntity);
			PublisherResp response = publishersMapper.toPublisherResponse(publisherEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}

	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<PublisherResp> findByName(String name) {
		Specification<PublisherEntity> spec = Specification.where(null);

		spec = spec.and(GameSpecifications.nameContains(name));
		
		List<PublisherEntity> publishersFound = publishersRepository.findAll(spec);

		if (!CollectionUtils.isEmpty(publishersFound)) {
			logger.info(LoggerConstants.PUBLISHER_ID_FOUND, publishersFound);
			List<PublisherResp> response = publishersMapper.toPublishersResponse(publishersFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<PublisherEntity> findAllById(Set<Long> ids) {
		// find publishers
		Set<PublisherEntity> publishers = new HashSet<>();
		
		publishers.addAll(publishersRepository.findAllById(ids));
		
		return publishers;
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<PublisherResp> findAll(Pageable pageable) {
		Page<PublisherEntity> publishersFound =  publishersRepository.findAll(pageable);
		
		// prepare response
		List<PublisherResp> response = publishersMapper.toPublishersResponse(publishersFound.getContent());
		
		// convert response for pagination
		Page<PublisherResp> responsePage = new PageImpl<>(response, pageable, publishersFound.getTotalElements()); 
		
		return new CollectionResponse<>(responsePage);
	}

	@Override
	public CollectionResponse<PublisherResp> create(PublisherReqPost publisherReqPost) {
		PublisherEntity publisherEntity = new PublisherEntity();
		publisherEntity.setName(publisherReqPost.getName());

		publisherEntity = publishersRepository.save(publisherEntity);

		PublisherResp response = publishersMapper.toPublisherResponse(publisherEntity);
		logger.info(LoggerConstants.PUBLISHER_CREATED, publisherEntity);

		return new CollectionResponse<>(response);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "publishersCache", key = "#id")
	public CollectionResponse<PublisherResp> deleteById(Long id) {
		PublisherEntity publisherEntity = publishersRepository.findById(id).orElseThrow();

		publishersRepository.deleteById(id);

		logger.info(LoggerConstants.PUBLISHER_DELETED, publisherEntity);

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "publishersCache", allEntries = true)
	public CollectionResponse<PublisherResp> deleteByName(String name) {
		PublisherEntity publisherEntity = publishersRepository.findByName(name).orElseThrow();

		publishersRepository.deleteById(publisherEntity.getId());;

		logger.info(LoggerConstants.PUBLISHER_DELETED, publisherEntity);

		return new CollectionResponse<>();
	}
	
	@Override
	//@CacheEvict(value = "publishersCache", key = "#id")
	public CollectionResponse<PublisherResp> updateById(Long id, PublisherReqPatch publisherReqPatch) {
		PublisherEntity publisherEntity = publishersRepository.findById(id).orElseThrow();
		publisherEntity.setName(publisherReqPatch.getName());
		publisherEntity = publishersRepository.save(publisherEntity);

		PublisherResp response = publishersMapper.toPublisherResponse(publisherEntity);
		logger.info(LoggerConstants.PUBLISHER_UPDATED, publisherEntity);

		return new CollectionResponse<>(response);
	}
}

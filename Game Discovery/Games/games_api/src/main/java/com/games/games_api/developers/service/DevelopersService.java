	package com.games.games_api.developers.service;

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
import com.games.games_api.developers.dto.DeveloperReqPatch;
import com.games.games_api.developers.dto.DeveloperReqPost;
import com.games.games_api.developers.dto.DeveloperResp;
import com.games.games_api.developers.entity.DeveloperEntity;
import com.games.games_api.developers.mapper.DevelopersMapper;
import com.games.games_api.developers.repository.DevelopersRepository;
import com.games.games_api.games.specifications.GameSpecifications;
import com.games.games_api.utils.LoggerConstants;

@Service
public class DevelopersService implements IDevelopersService {

	private static final Logger logger = LoggerFactory.getLogger(DevelopersService.class);

	@Autowired
	private DevelopersRepository developersRepository;

	private DevelopersMapper developersMapper = Mappers.getMapper(DevelopersMapper.class);

	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "developersCache", key = "#id")
	public CollectionResponse<DeveloperResp> findById(Long id) {
		DeveloperEntity developerEntity = developersRepository.findById(id).orElse(null);

		if (developerEntity != null) {
			logger.info(LoggerConstants.DEVELOPER_ID_FOUND, developerEntity);
			DeveloperResp response = developersMapper.toDeveloperResponse(developerEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}

	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<DeveloperResp> findByName(String name) {
		Specification<DeveloperEntity> spec = Specification.where(null);

		spec = spec.and(GameSpecifications.nameContains(name));
		
		List<DeveloperEntity> developersFound = developersRepository.findAll(spec);

		if (!CollectionUtils.isEmpty(developersFound)) {
			logger.info(LoggerConstants.DEVELOPER_ID_FOUND, developersFound);
			List<DeveloperResp> response = developersMapper.toDevelopersResponse(developersFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<DeveloperEntity> findAllById(Set<Long> ids) {
		// find genres
		Set<DeveloperEntity> developers = new HashSet<>();

		developers.addAll(developersRepository.findAllById(ids));

		return developers;
	}

	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<DeveloperResp> findAll(Pageable pageable) {
		Page<DeveloperEntity> publishersFound = developersRepository.findAll(pageable);

		// prepare response
		List<DeveloperResp> response = developersMapper.toDevelopersResponse(publishersFound.getContent());

		// convert response for pagination
		Page<DeveloperResp> responsePage = new PageImpl<>(response, pageable, publishersFound.getTotalElements());

		return new CollectionResponse<>(responsePage);
	}

	@Override
	public CollectionResponse<DeveloperResp> create(DeveloperReqPost developerReqPost) {
		DeveloperEntity developerEntity = new DeveloperEntity();
		developerEntity.setName(developerReqPost.getName());

		developerEntity = developersRepository.save(developerEntity);

		DeveloperResp response = developersMapper.toDeveloperResponse(developerEntity);
		logger.info(LoggerConstants.DEVELOPER_CREATED, developerEntity);

		return new CollectionResponse<>(response);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "developersCache", key = "#id")
	public CollectionResponse<DeveloperResp> deleteById(Long id) {
		DeveloperEntity DeveloperEntity = developersRepository.findById(id).orElseThrow();

		developersRepository.deleteById(id);

		logger.info(LoggerConstants.DEVELOPER_DELETED, DeveloperEntity);

		return new CollectionResponse<>();
	}

	@Override
	@Transactional
	//@CacheEvict(value = "developersCache", allEntries = true)
	public CollectionResponse<DeveloperResp> deleteByName(String name) {
		DeveloperEntity developerEntity = developersRepository.findByName(name).orElseThrow();

		developersRepository.deleteById(developerEntity.getId());
		;

		logger.info(LoggerConstants.DEVELOPER_DELETED, developerEntity);

		return new CollectionResponse<>();
	}

	@Override
	//@CacheEvict(value = "developersCache", key = "#id")
	public CollectionResponse<DeveloperResp> updateById(Long id, DeveloperReqPatch developerReqPatch) {
		DeveloperEntity developerEntity = developersRepository.findById(id).orElseThrow();
		developerEntity.setName(developerReqPatch.getName());
		developerEntity = developersRepository.save(developerEntity);

		DeveloperResp response = developersMapper.toDeveloperResponse(developerEntity);
		logger.info(LoggerConstants.DEVELOPER_UPDATED, developerEntity);

		return new CollectionResponse<>(response);
	}
}

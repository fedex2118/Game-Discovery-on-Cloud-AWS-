package com.games.games_api.developers.service;

import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.developers.dto.DeveloperReqPatch;
import com.games.games_api.developers.dto.DeveloperReqPost;
import com.games.games_api.developers.dto.DeveloperResp;
import com.games.games_api.developers.entity.DeveloperEntity;

public interface IDevelopersService {

	CollectionResponse<DeveloperResp> findById(Long id);

	CollectionResponse<DeveloperResp> findByName(String name);

	CollectionResponse<DeveloperResp> create(DeveloperReqPost developerReqPost);

	CollectionResponse<DeveloperResp> deleteById(Long id);

	CollectionResponse<DeveloperResp> deleteByName(String name);

	CollectionResponse<DeveloperResp> updateById(Long id, DeveloperReqPatch developerReqPatch);

	Set<DeveloperEntity> findAllById(Set<Long> ids);

	CollectionResponse<DeveloperResp> findAll(Pageable pageable);

}

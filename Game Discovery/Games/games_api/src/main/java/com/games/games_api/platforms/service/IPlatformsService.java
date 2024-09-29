package com.games.games_api.platforms.service;

import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.platforms.dto.PlatformReqPatch;
import com.games.games_api.platforms.dto.PlatformReqPost;
import com.games.games_api.platforms.dto.PlatformResp;
import com.games.games_api.platforms.entity.PlatformEntity;

public interface IPlatformsService {

	CollectionResponse<PlatformResp> findById(Long id);

	CollectionResponse<PlatformResp> findByName(String name);

	CollectionResponse<PlatformResp> create(PlatformReqPost platformReqPost);

	CollectionResponse<PlatformResp> deleteById(Long id);

	CollectionResponse<PlatformResp> deleteByName(String name);

	CollectionResponse<PlatformResp> updateById(Long id, PlatformReqPatch platformReqPatch);

	Set<PlatformEntity> findAllById(Set<Long> ids);

	CollectionResponse<PlatformResp> findAll(Pageable pageable);

}

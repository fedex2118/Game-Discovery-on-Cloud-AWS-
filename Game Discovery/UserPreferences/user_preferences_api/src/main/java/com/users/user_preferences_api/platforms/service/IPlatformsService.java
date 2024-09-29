package com.users.user_preferences_api.platforms.service;

import org.springframework.data.domain.Pageable;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.platforms.dto.PlatformReqPost;
import com.users.user_preferences_api.platforms.dto.PlatformResp;

public interface IPlatformsService {

	CollectionResponse<PlatformResp> create(PlatformReqPost platformReqPost);
	
	CollectionResponse<PlatformResp> findByKey(Long platformId, Long userId);

	CollectionResponse<PlatformResp> findAllByUserId(Long userId, Pageable pageable);

	CollectionResponse<PlatformResp> deleteUserPlatforms(Long userId);

	CollectionResponse<PlatformResp> deleteByKey(Long userId, Long platformId);

	CollectionResponse<PlatformResp> deletePlatforms(Long platformId);

}

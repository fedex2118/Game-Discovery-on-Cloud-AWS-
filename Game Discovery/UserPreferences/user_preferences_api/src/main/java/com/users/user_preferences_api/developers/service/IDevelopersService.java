package com.users.user_preferences_api.developers.service;

import org.springframework.data.domain.Pageable;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.developers.dto.DeveloperReqPost;
import com.users.user_preferences_api.developers.dto.DeveloperResp;

public interface IDevelopersService {

	CollectionResponse<DeveloperResp> create(DeveloperReqPost developerReqPost);
	
	CollectionResponse<DeveloperResp> findByKey(Long developerId, Long userId);

	CollectionResponse<DeveloperResp> findAllByUserId(Long userId, Pageable pagaeable);

	CollectionResponse<DeveloperResp> deleteUserDevelopers(Long userId);

	CollectionResponse<DeveloperResp> deleteByKey(Long userId, Long developerId);

	CollectionResponse<DeveloperResp> deleteDevelopers(Long developerId);

}

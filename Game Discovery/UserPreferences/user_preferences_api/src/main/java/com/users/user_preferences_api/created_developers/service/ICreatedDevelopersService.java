package com.users.user_preferences_api.created_developers.service;

import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperReqPost;
import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperResp;
import com.users.user_preferences_api.custom.response.CollectionResponse;

public interface ICreatedDevelopersService {

	CollectionResponse<CreatedDeveloperResp> create(CreatedDeveloperReqPost createdDeveloperReqPost);

	CollectionResponse<CreatedDeveloperResp> update(Long gameId, Long userId);

	CollectionResponse<CreatedDeveloperResp> findByKey(Long gameId, Long userId);

	CollectionResponse<CreatedDeveloperResp> deleteByKey(Long gameId, Long userId);

	CollectionResponse<CreatedDeveloperResp> findByUserId(Long userId);

}

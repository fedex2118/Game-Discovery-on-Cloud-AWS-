package com.users.user_preferences_api.created_publishers.service;

import com.users.user_preferences_api.created_publishers.dto.CreatedPublisherReqPost;
import com.users.user_preferences_api.created_publishers.dto.CreatedPublisherResp;
import com.users.user_preferences_api.custom.response.CollectionResponse;

public interface ICreatedPublishersService {

	CollectionResponse<CreatedPublisherResp> create(CreatedPublisherReqPost createdPublisherReqPost);

	CollectionResponse<CreatedPublisherResp> update(Long gameId, Long userId);

	CollectionResponse<CreatedPublisherResp> findByKey(Long gameId, Long userId);

	CollectionResponse<CreatedPublisherResp> deleteByKey(Long gameId, Long userId);

	CollectionResponse<CreatedPublisherResp> findByUserId(Long userId);

}

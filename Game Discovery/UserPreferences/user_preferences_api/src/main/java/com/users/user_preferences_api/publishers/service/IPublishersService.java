package com.users.user_preferences_api.publishers.service;

import org.springframework.data.domain.Pageable;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.publishers.dto.PublisherReqPost;
import com.users.user_preferences_api.publishers.dto.PublisherResp;

public interface IPublishersService {

	CollectionResponse<PublisherResp> create(PublisherReqPost publisherReqPost);

	CollectionResponse<PublisherResp> findByKey(Long publisherId, Long userId);

	CollectionResponse<PublisherResp> findAllByUserId(Long userId, Pageable pageable);

	CollectionResponse<PublisherResp> deleteUserPublishers(Long userId);

	CollectionResponse<PublisherResp> deleteByKey(Long userId, Long publisherId);

	CollectionResponse<PublisherResp> deletePublishers(Long publisherId);

}

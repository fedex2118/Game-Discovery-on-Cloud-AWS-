package com.games.games_api.publishers.service;

import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.publishers.dto.PublisherReqPatch;
import com.games.games_api.publishers.dto.PublisherReqPost;
import com.games.games_api.publishers.dto.PublisherResp;
import com.games.games_api.publishers.entity.PublisherEntity;

public interface IPublishersService {

	CollectionResponse<PublisherResp> findById(Long id);

	CollectionResponse<PublisherResp> findByName(String name);

	CollectionResponse<PublisherResp> create(PublisherReqPost publisherReqPost);

	CollectionResponse<PublisherResp> deleteById(Long id);

	CollectionResponse<PublisherResp> deleteByName(String name);

	CollectionResponse<PublisherResp> updateById(Long id, PublisherReqPatch publisherReqPatch);

	Set<PublisherEntity> findAllById(Set<Long> ids);

	CollectionResponse<PublisherResp> findAll(Pageable pageable);

}

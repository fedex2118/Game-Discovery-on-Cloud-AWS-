package com.gateway.gateway_api.games.services;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.games.data.classes.PublisherReqPatch;
import com.gateway.gateway_api.games.data.classes.PublisherReqPost;
import com.gateway.gateway_api.games.data.classes.PublisherResp;

public interface IPublishersService {
	
	CollectionResponse<PublisherResp> create(PublisherReqPost request);

	CollectionResponse<PublisherResp> updateById(Long id, PublisherReqPatch request);

	CollectionResponse<PublisherResp> findById(Long id);

	CollectionResponse<PublisherResp> findByName(String name);

}

package com.gateway.gateway_api.games.services;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPatch;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPost;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;

public interface IDevelopersService {
	
	CollectionResponse<DeveloperResp> create(DeveloperReqPost request);

	CollectionResponse<DeveloperResp> updateById(Long id, DeveloperReqPatch request);

	CollectionResponse<DeveloperResp> findById(Long id);

	CollectionResponse<DeveloperResp> findByName(String name);

}

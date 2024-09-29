package com.gateway.gateway_api.games.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPost;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;
import com.gateway.gateway_api.games.data.classes.UpdatableGameResp;

public interface IUpdatableGameService {

	CollectionResponse<UpdatableGameResp> findById(Long id);

	CollectionResponse<UpdatableGameResp> create(UpdatableGameReqPost gameReqPost);

	CollectionResponse<UpdatableGameResp> update(Long id, UpdatableGameReqPut gameReqPut);

	CollectionResponse<UpdatableGameResp> deleteById(Long id);

	CollectionResponse<UpdatableGameResp> findAll(Pageable pageable);

	CollectionResponse<UpdatableGameResp> deleteAllByIds(List<Long> ids);

	

	
	
}

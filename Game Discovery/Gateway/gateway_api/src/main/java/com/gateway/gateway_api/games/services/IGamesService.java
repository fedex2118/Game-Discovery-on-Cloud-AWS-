package com.gateway.gateway_api.games.services;

import org.springframework.data.domain.Pageable;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.games.data.classes.GameCriteria;
import com.gateway.gateway_api.games.data.classes.GameReqPost;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;
import com.gateway.gateway_api.games.data.classes.UpdatableGameResp;
import com.gateway.gateway_api.games.request.dto.GamePutDevsAndPublishers;

public interface IGamesService {


	CollectionResponse<GameResp> findById(Long id);
	
	CollectionResponse<GameResp> findByCriteria(GameCriteria gameReqCriteria, Pageable pageable);

	CollectionResponse<GameResp> create(GameReqPost gameReqPost);

	CollectionResponse<UpdatableGameResp> sendUpdateSuggestion(Long id, UpdatableGameReqPut gameReqPut);

	CollectionResponse<GameResp> updateGameDevsAndPublishers(Long id, GamePutDevsAndPublishers request);


}

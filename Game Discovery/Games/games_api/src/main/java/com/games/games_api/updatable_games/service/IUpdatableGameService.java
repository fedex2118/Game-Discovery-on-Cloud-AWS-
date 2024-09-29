package com.games.games_api.updatable_games.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPost;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPut;
import com.games.games_api.updatable_games.dto.UpdatableGameResp;

public interface IUpdatableGameService {

	CollectionResponse<UpdatableGameResp> findById(Long id);

	CollectionResponse<UpdatableGameResp> create(UpdatableGameReqPost gameReqPost);

	CollectionResponse<UpdatableGameResp> update(Long id, UpdatableGameReqPut gameReqPut);

	CollectionResponse<UpdatableGameResp> deleteById(Long id);

	CollectionResponse<UpdatableGameResp> findAll(Pageable pageable);

	CollectionResponse<UpdatableGameResp> deleteAllByIds(List<Long> ids);

	

	
	
}

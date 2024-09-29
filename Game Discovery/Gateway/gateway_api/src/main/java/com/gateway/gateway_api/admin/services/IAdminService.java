package com.gateway.gateway_api.admin.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPatch;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPost;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.GameReqPatchStatus;
import com.gateway.gateway_api.games.data.classes.GameReqPut;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GameRespTimestamp;
import com.gateway.gateway_api.games.data.classes.GameStatusResp;
import com.gateway.gateway_api.games.data.classes.PublisherReqPatch;
import com.gateway.gateway_api.games.data.classes.PublisherReqPost;
import com.gateway.gateway_api.games.data.classes.PublisherResp;

public interface IAdminService {

	CollectionResponse<Long> synchAllGamesWithReviews();

	CollectionResponse<Long> synchInputGamesWithReviews(List<Long> gameIds);

	CollectionResponse<GameResp> deleteAllGamesByIds(List<Long> gameIds);

	CollectionResponse<GameStatusResp> patchStatus(List<GameReqPatchStatus> gameReqPatchStatusList);

	CollectionResponse<GameRespTimestamp> findAllGamesOnPending(Pageable pageable);
	
	CollectionResponse<DeveloperResp> createDeveloper(DeveloperReqPost request);

	CollectionResponse<DeveloperResp> updateDeveloperById(Long id, DeveloperReqPatch request);

	CollectionResponse<PublisherResp> createPublisher(PublisherReqPost request);

	CollectionResponse<PublisherResp> updatePublisherById(Long id, PublisherReqPatch request);

	CollectionResponse<GameResp> updateGame(Long id, GameReqPut request);

}

package com.games.games_api.games.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.games.dto.GameCriteria;
import com.games.games_api.games.dto.GameCriteriaDiscovery;
import com.games.games_api.games.dto.GameReqPatch;
import com.games.games_api.games.dto.GameReqPatchStatus;
import com.games.games_api.games.dto.GameReqPost;
import com.games.games_api.games.dto.GameReqPut;
import com.games.games_api.games.dto.GameResp;
import com.games.games_api.games.dto.GameRespTimestamp;
import com.games.games_api.games.dto.GameStatusResp;

public interface IGamesService {

	CollectionResponse<GameResp> findById(Long id);

	CollectionResponse<GameResp> findByCriteria(GameCriteria gameReqCriteria, Pageable pageable);

	CollectionResponse<Long> findAllIds();
	
	CollectionResponse<GameResp> create(GameReqPost gameReqPost);

	/**
	 * We treat PUT method here as partial update and not total update. 
	 * This means that only passed parameters will be replaced on the entity.
	 * There is an exception for the entities on the bridge tables such as game_developers, 
	 * game_publishers and so on... 
	 * For these list in input we apply the following logic:
	 * 1) if no parameter is passed -> nothing is updated on db
	 * 2) if empty list is passed -> complete wipe on the bridge table
	 * 3) if list with values are passed -> only those values are saved on db the others are wiped
	 * @param id
	 * @param gameReqPut
	 * @return
	 * @author fedex2118
	 */
	CollectionResponse<GameResp> update(Long id, GameReqPut gameReqPut);

	CollectionResponse<GameResp> patch(Long id, GameReqPatch gameReqPatch);

	CollectionResponse<GameResp> deleteById(Long id);

	/**
	 * For administrators
	 * @param pageable
	 * @return
	 */
	CollectionResponse<GameRespTimestamp> findAllGamesOnPending(Pageable pageable);

	CollectionResponse<GameStatusResp> patchStatus(List<GameReqPatchStatus> gameReqPatchStatusList);

	CollectionResponse<GameStatusResp> findAllGamesOnRejected(Pageable pageable);

	CollectionResponse<GameResp> deleteAllGamesByIds(List<Long> gameIds);

	CollectionResponse<Long> getPendingGamesIds(List<Long> gameIds);

	CollectionResponse<GameStatusResp> findGameStatus(Long id);

	CollectionResponse<GameResp> findByCriteriaDiscovery(GameCriteriaDiscovery gameReqCriteria, Pageable pageable);

	CollectionResponse<GameResp> findAllByIds(Set<Long> ids);

}

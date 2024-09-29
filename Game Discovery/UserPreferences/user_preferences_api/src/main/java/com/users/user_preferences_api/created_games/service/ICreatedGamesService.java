package com.users.user_preferences_api.created_games.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.users.user_preferences_api.created_games.dto.CreatedGameReqPost;
import com.users.user_preferences_api.created_games.dto.CreatedGameResp;
import com.users.user_preferences_api.custom.response.CollectionResponse;

public interface ICreatedGamesService {

	CollectionResponse<CreatedGameResp> create(CreatedGameReqPost createdGameReqPost);
	
	CollectionResponse<CreatedGameResp> findByKey(Long gameId, Long userId);

	CollectionResponse<CreatedGameResp> findAllByUserId(Long userId, Pageable pageable);

	CollectionResponse<CreatedGameResp> deleteUserGames(Long userId);

	CollectionResponse<CreatedGameResp> deleteByKey(Long userId, Long gameId);

	CollectionResponse<CreatedGameResp> deleteGames(Long gameId);

	CollectionResponse<CreatedGameResp> findAllByUserIdLastFortyEightHours(Long userId, Pageable pageable);

	CollectionResponse<CreatedGameResp> update(Long gameId, Long userId);

	CollectionResponse<CreatedGameResp> deleteGamesWithIds(List<Long> gameIds);

}

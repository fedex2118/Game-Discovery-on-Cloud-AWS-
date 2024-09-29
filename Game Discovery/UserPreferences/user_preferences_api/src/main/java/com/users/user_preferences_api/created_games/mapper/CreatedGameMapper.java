package com.users.user_preferences_api.created_games.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.created_games.dto.CreatedGameResp;
import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;

@Mapper
public interface CreatedGameMapper {
	
	@Mapping(target = "userId", source = "userPreference.id")
	CreatedGameResp toCreatedGameResponse(CreatedGameEntity createdGameEntity);

	List<CreatedGameResp> toCreatedGamesResponse(List<CreatedGameEntity> createdGameEntities);
}

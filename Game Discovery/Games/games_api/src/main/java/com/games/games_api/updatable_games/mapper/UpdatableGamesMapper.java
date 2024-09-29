package com.games.games_api.updatable_games.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.games.games_api.games.entity.GameEntity;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPost;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPut;
import com.games.games_api.updatable_games.dto.UpdatableGameResp;
import com.games.games_api.updatable_games.entity.UpdatableGameEntity;

@Mapper
public interface UpdatableGamesMapper {

	UpdatableGameResp toUpdatableGameResponse(UpdatableGameEntity gameEntity);
	
	List<UpdatableGameResp> toUpdatableGamesResponse(List<UpdatableGameEntity> gameEntities);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void toUpdatableGameEntity(UpdatableGameReqPost gameReqPost, @MappingTarget UpdatableGameEntity entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void fromGameEntity(GameEntity gameEntity, @MappingTarget UpdatableGameEntity entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void toUpdatableGameEntity(UpdatableGameReqPut gameReqPut, @MappingTarget UpdatableGameEntity entity);
}

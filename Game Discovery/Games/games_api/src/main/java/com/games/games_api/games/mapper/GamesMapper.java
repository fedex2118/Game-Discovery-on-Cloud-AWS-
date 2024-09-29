package com.games.games_api.games.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.games.games_api.developers.entity.DeveloperEntity;
import com.games.games_api.games.dto.GameReqPatch;
import com.games.games_api.games.dto.GameReqPost;
import com.games.games_api.games.dto.GameReqPut;
import com.games.games_api.games.dto.GameResp;
import com.games.games_api.games.dto.GameStatus;
import com.games.games_api.games.dto.GameStatusResp;
import com.games.games_api.games.entity.GameEntity;
import com.games.games_api.genres.entity.GenreEntity;
import com.games.games_api.platforms.entity.PlatformEntity;
import com.games.games_api.publishers.dto.PublisherResp;
import com.games.games_api.publishers.entity.PublisherEntity;

@Mapper
public interface GamesMapper {

	@Mapping(target = "status", source = "status", qualifiedByName = "toGameStatus")
	GameResp toGameResponse(GameEntity gameEntity);
	
	List<GameResp> toGamesResponse(List<GameEntity> gameEntities);
	
	@Mapping(target = "status", source = "status", qualifiedByName = "toGameStatus")
	GameStatusResp toGameResponseStatus(GameEntity gameEntity);
	
	List<GameStatusResp> toGamesResponseStatus(List<GameEntity> gameEntities);
	
	GameEntity toGameEntity(GameReqPost gameReqPost);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void toGameEntity(GameReqPut gameReqPut, @MappingTarget GameEntity entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void toGameEntity(GameReqPatch gameReqPatch, @MappingTarget GameEntity entity);
	
	@Named("toGameStatus")
	default GameStatus toGameStatus(int status) {
		GameStatus gameStatus;
		
		switch(status) {
		case 0 -> gameStatus = GameStatus.PENDING;
		case 1 -> gameStatus = GameStatus.APPROVED;
		case 2 -> gameStatus = GameStatus.REJECTED;
		default -> gameStatus = null;
		}
		
		return gameStatus;
	}
	
}

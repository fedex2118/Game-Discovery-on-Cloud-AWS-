package com.games.games_api.games.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.games.games_api.games.dto.GameRespTimestamp;
import com.games.games_api.games.dto.GameStatus;
import com.games.games_api.games.entity.GameEntity;

@Mapper
public interface GamesTimestampMapper {

	@Mapping(target = "status", source = "status", qualifiedByName = "toGameStatus")
	GameRespTimestamp toGameResponseTimestamp(GameEntity gameEntity);
	
	List<GameRespTimestamp> toGamesResponseTimestamp(List<GameEntity> gameEntities);
	
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

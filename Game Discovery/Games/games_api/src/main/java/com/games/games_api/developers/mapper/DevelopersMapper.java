package com.games.games_api.developers.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.games.games_api.developers.dto.DeveloperResp;
import com.games.games_api.developers.entity.DeveloperEntity;

@Mapper
public interface DevelopersMapper {
	
	DeveloperResp toDeveloperResponse(DeveloperEntity developerEntity);

	List<DeveloperResp> toDevelopersResponse(List<DeveloperEntity> developerEntities);
}

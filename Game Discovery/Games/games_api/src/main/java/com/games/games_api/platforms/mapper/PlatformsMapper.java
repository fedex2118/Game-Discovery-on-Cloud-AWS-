package com.games.games_api.platforms.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.games.games_api.platforms.dto.PlatformResp;
import com.games.games_api.platforms.entity.PlatformEntity;

@Mapper
public interface PlatformsMapper {
	
	PlatformResp toPlatformResponse(PlatformEntity platformEntity);

	List<PlatformResp> toPlatformsResponse(List<PlatformEntity> platformEntities);
}

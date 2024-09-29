package com.users.user_preferences_api.platforms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.platforms.dto.PlatformResp;
import com.users.user_preferences_api.platforms.entity.PlatformEntity;

@Mapper
public interface PlatformsMapper {
	
	@Mapping(target = "userId", source = "id.userId")
	@Mapping(target = "platformId", source = "id.platformId")
	PlatformResp toPlatformResponse(PlatformEntity platformEntity);

	List<PlatformResp> toPlatformsResponse(List<PlatformEntity> platformEntities);
}

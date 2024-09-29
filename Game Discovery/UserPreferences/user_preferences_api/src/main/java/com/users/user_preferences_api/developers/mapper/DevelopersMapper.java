package com.users.user_preferences_api.developers.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.developers.dto.DeveloperResp;
import com.users.user_preferences_api.developers.entity.DeveloperEntity;

@Mapper
public interface DevelopersMapper {
	
	@Mapping(target = "userId", source = "id.userId")
	@Mapping(target = "developerId", source = "id.developerId")
	DeveloperResp toDeveloperResponse(DeveloperEntity developerEntity);

	List<DeveloperResp> toDevelopersResponse(List<DeveloperEntity> developerEntities);
}

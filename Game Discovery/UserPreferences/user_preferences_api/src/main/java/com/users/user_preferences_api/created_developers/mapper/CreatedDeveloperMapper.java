package com.users.user_preferences_api.created_developers.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperResp;
import com.users.user_preferences_api.created_developers.entity.CreatedDeveloperEntity;

@Mapper
public interface CreatedDeveloperMapper {
	
	@Mapping(target = "userId", source = "userPreference.id")
	CreatedDeveloperResp toCreatedDeveloperResponse(CreatedDeveloperEntity createdGameEntity);

	List<CreatedDeveloperResp> toCreatedDevelopersResponse(List<CreatedDeveloperEntity> createdGameEntities);
}

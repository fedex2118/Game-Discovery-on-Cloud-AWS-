package com.users.user_preferences_api.created_publishers.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.created_publishers.dto.CreatedPublisherResp;
import com.users.user_preferences_api.created_publishers.entity.CreatedPublisherEntity;

@Mapper
public interface CreatedPublisherMapper {
	
	@Mapping(target = "userId", source = "userPreference.id")
	CreatedPublisherResp toCreatedPublisherResponse(CreatedPublisherEntity createdGameEntity);

	List<CreatedPublisherResp> toCreatedPublishersResponse(List<CreatedPublisherEntity> createdGameEntities);
}

package com.users.user_preferences_api.publishers.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.publishers.dto.PublisherResp;
import com.users.user_preferences_api.publishers.entity.PublisherEntity;

@Mapper
public interface PublishersMapper {
	
	@Mapping(target = "userId", source = "id.userId")
	@Mapping(target = "publisherId", source = "id.publisherId")
	PublisherResp toPublisherResponse(PublisherEntity publisherEntity);

	List<PublisherResp> toPublishersResponse(List<PublisherEntity> publisherEntities);
}

package com.games.games_api.publishers.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.games.games_api.publishers.dto.PublisherResp;
import com.games.games_api.publishers.entity.PublisherEntity;

@Mapper
public interface PublishersMapper {

	PublisherResp toPublisherResponse(PublisherEntity publisherEntity);

	List<PublisherResp> toPublishersResponse(List<PublisherEntity> publisherEntities);
}

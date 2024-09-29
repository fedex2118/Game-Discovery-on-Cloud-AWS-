package com.gateway.gateway_api.games.mapper;

import org.mapstruct.Mapper;

import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPost;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;

@Mapper
public interface UpdatableGamesMapper {

	UpdatableGameReqPost toUpdatableGameReqPost(UpdatableGameReqPut request);
}

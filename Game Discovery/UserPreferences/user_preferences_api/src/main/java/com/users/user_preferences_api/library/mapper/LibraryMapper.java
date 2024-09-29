package com.users.user_preferences_api.library.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.library.dto.LibraryResp;
import com.users.user_preferences_api.library.entity.LibraryEntity;

@Mapper
public interface LibraryMapper {
	
	@Mapping(target = "userId", source = "id.userId")
	@Mapping(target = "gameId", source = "id.gameId")
	LibraryResp toLibraryResponse(LibraryEntity libraryEntity);

	List<LibraryResp> toLibrariesResponse(List<LibraryEntity> libraryEntities);
}

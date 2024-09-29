package com.users.user_preferences_api.genres.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.users.user_preferences_api.genres.dto.GenreResp;
import com.users.user_preferences_api.genres.entity.GenreEntity;

@Mapper
public interface GenresMapper {
	
	@Mapping(target = "userId", source = "id.userId")
	@Mapping(target = "genreId", source = "id.genreId")
	GenreResp toGenreResponse(GenreEntity genreEntity);

	List<GenreResp> toGenresResponse(List<GenreEntity> genreEntities);
}

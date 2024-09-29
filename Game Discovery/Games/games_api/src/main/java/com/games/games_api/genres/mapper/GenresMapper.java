package com.games.games_api.genres.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.games.games_api.genres.dto.GenreResp;
import com.games.games_api.genres.entity.GenreEntity;

@Mapper
public interface GenresMapper {
	
	GenreResp toGenreResponse(GenreEntity genreEntity);

	List<GenreResp> toGenresResponse(List<GenreEntity> genreEntities);
}

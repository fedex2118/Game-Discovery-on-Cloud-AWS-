package com.games.games_api.genres.service;

import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.genres.dto.GenreReqPatch;
import com.games.games_api.genres.dto.GenreReqPost;
import com.games.games_api.genres.dto.GenreResp;
import com.games.games_api.genres.entity.GenreEntity;

public interface IGenresService {

	CollectionResponse<GenreResp> findById(Long id);

	CollectionResponse<GenreResp> findByName(String name);

	CollectionResponse<GenreResp> create(GenreReqPost genreReqPost);

	CollectionResponse<GenreResp> deleteById(Long id);

	CollectionResponse<GenreResp> deleteByName(String name);

	CollectionResponse<GenreResp> updateById(Long id, GenreReqPatch genreReqPatch);

	Set<GenreEntity> findAllById(Set<Long> ids);

	CollectionResponse<GenreResp> findAll(Pageable pageable);

}

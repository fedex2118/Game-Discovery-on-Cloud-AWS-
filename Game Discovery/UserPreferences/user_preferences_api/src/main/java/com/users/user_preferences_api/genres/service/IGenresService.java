package com.users.user_preferences_api.genres.service;

import org.springframework.data.domain.Pageable;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.genres.dto.GenreReqPost;
import com.users.user_preferences_api.genres.dto.GenreResp;

public interface IGenresService {

	CollectionResponse<GenreResp> create(GenreReqPost genreReqPost);

	CollectionResponse<GenreResp> findByKey(Long userId, Long genreId);

	CollectionResponse<GenreResp> findAllByUserId(Long userId, Pageable pageable);

	CollectionResponse<GenreResp> deleteUserGenres(Long userId);

	CollectionResponse<GenreResp> deleteByKey(Long userId, Long genreId);

	CollectionResponse<GenreResp> deleteGenres(Long genreId);

}

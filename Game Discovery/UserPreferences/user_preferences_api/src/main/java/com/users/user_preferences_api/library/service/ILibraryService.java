package com.users.user_preferences_api.library.service;

import org.springframework.data.domain.Pageable;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.library.dto.LibraryReqPost;
import com.users.user_preferences_api.library.dto.LibraryResp;

public interface ILibraryService {

	CollectionResponse<LibraryResp> create(LibraryReqPost libraryReqPost);
	
	CollectionResponse<LibraryResp> findByKey(Long gameId, Long userId);

	CollectionResponse<LibraryResp> findAllByUserId(Long userId, Pageable pageable);

	CollectionResponse<LibraryResp> deleteUserGames(Long userId);

	CollectionResponse<LibraryResp> deleteByKey(Long userId, Long gameId);

	CollectionResponse<LibraryResp> deleteGames(Long gameId);

}

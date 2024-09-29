package com.users.user_preferences_api.library.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.library.dto.LibraryReqPost;
import com.users.user_preferences_api.library.dto.LibraryResp;
import com.users.user_preferences_api.library.service.ILibraryService;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Library")
@RequestMapping(path = WebConstants.API_V1)
public class LibraryController {

	@Autowired
	private ILibraryService libraryService;

	@PostMapping(path = WebConstants.LIBRARY + WebConstants.GAME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<LibraryResp>> create(
			@Valid @RequestBody LibraryReqPost libraryReqPost) {
		return ResponseEntity.ok(libraryService.create(libraryReqPost));
	}
	
	@GetMapping(path = WebConstants.LIBRARY + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<LibraryResp>> findByKey(
			@PathVariable Long gameId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(libraryService.findByKey(gameId, userId));
	}
	
	@GetMapping(path = WebConstants.LIBRARY + WebConstants.GAMES + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<LibraryResp>> findAllByUserId(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(libraryService.findAllByUserId(userId, pageable));
	}
	
	@DeleteMapping(path = WebConstants.LIBRARY + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<LibraryResp>> deleteByKey(
			@PathVariable Long gameId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(libraryService.deleteByKey(gameId, userId));
	}
	
	@DeleteMapping(path = WebConstants.LIBRARY + WebConstants.GAMES + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<LibraryResp>> deleteUserGames(
			@PathVariable Long userId) {
		return ResponseEntity.ok(libraryService.deleteUserGames(userId));
	}
	
	@DeleteMapping(path = WebConstants.LIBRARY + WebConstants.GAMES + "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<LibraryResp>> deleteGames(
			@PathVariable Long gameId) {
		return ResponseEntity.ok(libraryService.deleteGames(gameId));
	}
}

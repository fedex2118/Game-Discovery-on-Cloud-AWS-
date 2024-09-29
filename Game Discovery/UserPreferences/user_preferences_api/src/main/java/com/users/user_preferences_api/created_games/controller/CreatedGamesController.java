package com.users.user_preferences_api.created_games.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.users.user_preferences_api.created_games.dto.CreatedGameReqPost;
import com.users.user_preferences_api.created_games.dto.CreatedGameResp;
import com.users.user_preferences_api.created_games.service.ICreatedGamesService;
import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Created Games")
@RequestMapping(path = WebConstants.API_V1)
public class CreatedGamesController {

	@Autowired
	private ICreatedGamesService createdGamesService;

	@PostMapping(path = WebConstants.CREATED + WebConstants.GAME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> create(
			@Valid @RequestBody CreatedGameReqPost createdGamesReqPost) {
		return ResponseEntity.ok(createdGamesService.create(createdGamesReqPost));
	}
	
	@PutMapping(path = WebConstants.CREATED + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> update(
			@PathVariable Long gameId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdGamesService.update(gameId, userId));
	}
	
	@GetMapping(path = WebConstants.CREATED + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> findByKey(
			@PathVariable Long gameId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdGamesService.findByKey(gameId, userId));
	}
	
	@GetMapping(path = WebConstants.CREATED + WebConstants.GAMES + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> findAllByUserId(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(createdGamesService.findAllByUserId(userId, pageable));
	}
	
	@GetMapping(path = WebConstants.CREATED + WebConstants.GAMES + "/user/{userId}/last-forty-eight-hours", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> findAllByUserIdLastFortyEightHours(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10,
					sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(createdGamesService.findAllByUserIdLastFortyEightHours(userId, pageable));
	}
	
	@DeleteMapping(path = WebConstants.CREATED + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> deleteByKey(
			@PathVariable Long gameId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdGamesService.deleteByKey(gameId, userId));
	}
	
	@DeleteMapping(path = WebConstants.CREATED + WebConstants.GAMES + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> deleteUserGames(
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdGamesService.deleteUserGames(userId));
	}
	
	@DeleteMapping(path = WebConstants.CREATED + WebConstants.GAMES + "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> deleteGames(
			@PathVariable Long gameId) {
		return ResponseEntity.ok(createdGamesService.deleteGames(gameId));
	}
	
	@DeleteMapping(path = WebConstants.CREATED + WebConstants.GAMES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedGameResp>> deleteGamesWithIds(
			@RequestParam List<Long> gameIds) {
		return ResponseEntity.ok(createdGamesService.deleteGamesWithIds(gameIds));
	}
}

package com.games.games_api.updatable_games.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPost;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPut;
import com.games.games_api.updatable_games.dto.UpdatableGameResp;
import com.games.games_api.updatable_games.service.IUpdatableGameService;
import com.games.games_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Updatable Games")
@RequestMapping(WebConstants.API_V1 + WebConstants.UPDATABLE)
public class UpdatableGamesController {
	
	@Autowired
	private IUpdatableGameService updatableGameService;
	
	@GetMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(updatableGameService.findById(id));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = WebConstants.GAMES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> findAll(
			@ParameterObject @PageableDefault(size = 10)
			@SortDefaults({
                @SortDefault(sort = "timestamp", direction = Sort.Direction.ASC)
        }) Pageable pageable) {
		return ResponseEntity.ok(updatableGameService.findAll(pageable));
	}

	
	@PostMapping(path = WebConstants.GAME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> create(
			@Valid @RequestBody UpdatableGameReqPost gameReqPost) {
		return ResponseEntity.ok(updatableGameService.create(gameReqPost));
	}
	
	@PutMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody UpdatableGameReqPut gameReqPut) {
		return ResponseEntity.ok(updatableGameService.update(id, gameReqPut));
	}

	
	@DeleteMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> deleteById(
			@PathVariable Long id) {
		return ResponseEntity.ok(updatableGameService.deleteById(id));
	}
	
	@DeleteMapping(path = WebConstants.GAMES + "/by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> deleteAllByIds(
			@RequestParam List<Long> ids) {
		return ResponseEntity.ok(updatableGameService.deleteAllByIds(ids));
	}
	
//	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('GATEWAY')")
//	@DeleteMapping(path = WebConstants.GAMES, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<CollectionResponse<UpdatableGameResp>> deleteAllGamesByIds(
//			@RequestParam List<Long> gameIds) {
//		return ResponseEntity.ok(updatableGameService.deleteAllGamesByIds(gameIds));
//	}
	
}

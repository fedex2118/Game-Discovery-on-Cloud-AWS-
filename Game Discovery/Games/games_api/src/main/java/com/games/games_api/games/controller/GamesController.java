package com.games.games_api.games.controller;

import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.games.dto.GameCriteria;
import com.games.games_api.games.dto.GameCriteriaDiscovery;
import com.games.games_api.games.dto.GameReqPatch;
import com.games.games_api.games.dto.GameReqPatchStatus;
import com.games.games_api.games.dto.GameReqPost;
import com.games.games_api.games.dto.GameReqPut;
import com.games.games_api.games.dto.GameResp;
import com.games.games_api.games.dto.GameRespTimestamp;
import com.games.games_api.games.dto.GameStatusResp;
import com.games.games_api.games.service.IGamesService;
import com.games.games_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Games")
@RequestMapping(WebConstants.API_V1)
public class GamesController {
	
	@Autowired
	private IGamesService gamesService;
	
	@GetMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(gamesService.findById(id));
	}
	
	@GetMapping(path = WebConstants.GAMES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> findByCriteria(
			@Valid @ParameterObject GameCriteria gameCriteria,
			@ParameterObject @PageableDefault(size = 10)
			@SortDefaults({
                @SortDefault(sort = "name", direction = Sort.Direction.ASC),
                @SortDefault(sort = "releaseDate", direction = Sort.Direction.DESC),
                @SortDefault(sort = "price", direction = Sort.Direction.ASC)
        }) Pageable pageable
			) {
		return ResponseEntity.ok(gamesService.findByCriteria(gameCriteria, pageable));
	}
	
	@GetMapping(path = WebConstants.GAMES + "/by-discovery-criteria", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> findByCriteriaDiscovery(
			@Valid @ParameterObject GameCriteriaDiscovery gameCriteria,
			@ParameterObject @PageableDefault(size = 10)
			@SortDefaults({
                @SortDefault(sort = "name", direction = Sort.Direction.ASC),
                @SortDefault(sort = "releaseDate", direction = Sort.Direction.DESC),
                @SortDefault(sort = "price", direction = Sort.Direction.ASC)
        }) Pageable pageable
			) {
		return ResponseEntity.ok(gamesService.findByCriteriaDiscovery(gameCriteria, pageable));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = WebConstants.GAMES + "/by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> findAllByIds(
			@RequestParam Set<Long> ids) {
		return ResponseEntity.ok(gamesService.findAllByIds(ids));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = WebConstants.GAMES + "/ids", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<Long>> findAllIds() {
		return ResponseEntity.ok(gamesService.findAllIds());
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = WebConstants.GAMES + "/with-pending-status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameRespTimestamp>> findAllGamesOnPending(
			@ParameterObject @PageableDefault(size = 10)
			@SortDefaults({
                @SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)
        }) Pageable pageable) {
		return ResponseEntity.ok(gamesService.findAllGamesOnPending(pageable));
	}
	
	@GetMapping(path = WebConstants.GAME + "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameStatusResp>> findGameStatus(
			@PathVariable Long id) {
		return ResponseEntity.ok(gamesService.findGameStatus(id));
	}
	
	@GetMapping(path = WebConstants.GAMES + "/ids/with-pending-status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<Long>> getPendingGamesIds(
			@RequestParam List<Long> gameIds) {
		return ResponseEntity.ok(gamesService.getPendingGamesIds(gameIds));
	}
	
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('GATEWAY')")
	@GetMapping(path = WebConstants.GAMES + "/with-rejected-status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameStatusResp>> findAllGamesOnRejected(
			@ParameterObject @PageableDefault(size = 1000)
			@SortDefaults({
                @SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)
        }) Pageable pageable) {
		return ResponseEntity.ok(gamesService.findAllGamesOnRejected(pageable));
	}
	
	@PostMapping(path = WebConstants.GAME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> create(
			@Valid @RequestBody GameReqPost gameReqPost) {
		return ResponseEntity.ok(gamesService.create(gameReqPost));
	}
	
	@PutMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody GameReqPut gameReqPut) {
		return ResponseEntity.ok(gamesService.update(id, gameReqPut));
	}
	
	@PatchMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> patch(
			@PathVariable Long id,
			@Valid @RequestBody GameReqPatch gameReqPatch) {
		return ResponseEntity.ok(gamesService.patch(id, gameReqPatch));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.GAMES + "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameStatusResp>> patchStatus(
			@Valid @RequestBody List<GameReqPatchStatus> gameReqPatchList) {
		return ResponseEntity.ok(gamesService.patchStatus(gameReqPatchList));
	}
	
	@DeleteMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> deleteById(
			@PathVariable Long id) {
		return ResponseEntity.ok(gamesService.deleteById(id));
	}
	
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('GATEWAY')")
	@DeleteMapping(path = WebConstants.GAMES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> deleteAllGamesByIds(
			@RequestParam List<Long> gameIds) {
		return ResponseEntity.ok(gamesService.deleteAllGamesByIds(gameIds));
	}
	
}

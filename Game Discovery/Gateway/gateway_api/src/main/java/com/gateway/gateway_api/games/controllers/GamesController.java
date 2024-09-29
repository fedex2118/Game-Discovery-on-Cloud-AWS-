package com.gateway.gateway_api.games.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.games.data.classes.GameCriteria;
import com.gateway.gateway_api.games.data.classes.GameReqPost;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;
import com.gateway.gateway_api.games.data.classes.UpdatableGameResp;
import com.gateway.gateway_api.games.request.dto.GamePutDevsAndPublishers;
import com.gateway.gateway_api.games.services.IGamesService;

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
	
	@PostMapping(path = WebConstants.GAME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> create(
			@Valid @RequestBody GameReqPost gameReqPost) {
		return ResponseEntity.ok(gamesService.create(gameReqPost));
	}
	
	@PutMapping(path = WebConstants.GAME + "/devs-and-publishers" + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody GamePutDevsAndPublishers request) {
		return ResponseEntity.ok(gamesService.updateGameDevsAndPublishers(id, request));
	}
	
	@PutMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody UpdatableGameReqPut gameReqPut) {
		return ResponseEntity.ok(gamesService.sendUpdateSuggestion(id, gameReqPut));
	}
}
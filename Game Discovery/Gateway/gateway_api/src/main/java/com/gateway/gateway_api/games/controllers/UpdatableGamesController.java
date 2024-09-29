package com.gateway.gateway_api.games.controllers;

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

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPost;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;
import com.gateway.gateway_api.games.data.classes.UpdatableGameResp;
import com.gateway.gateway_api.games.services.IUpdatableGameService;

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
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.GAMES + "/by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UpdatableGameResp>> deleteAllByIds(
			@RequestParam List<Long> ids) {
		return ResponseEntity.ok(updatableGameService.deleteAllByIds(ids));
	}
	
}

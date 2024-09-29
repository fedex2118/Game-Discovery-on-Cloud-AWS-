package com.games.games_api.developers.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.developers.dto.DeveloperReqPatch;
import com.games.games_api.developers.dto.DeveloperReqPost;
import com.games.games_api.developers.dto.DeveloperResp;
import com.games.games_api.developers.service.IDevelopersService;
import com.games.games_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Developers")
@RequestMapping(path = WebConstants.API_V1)
public class DevelopersController {

	@Autowired
	private IDevelopersService developersService;
	
	@GetMapping(path = WebConstants.DEVELOPER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(developersService.findById(id));
	}
	
	@GetMapping(path = WebConstants.DEVELOPER + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> findByName(@PathVariable String name) {
		return ResponseEntity.ok(developersService.findByName(name));
	}
	
	@GetMapping(path = WebConstants.DEVELOPERS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> findAll(
			@ParameterObject @PageableDefault(size = 10)
			@SortDefaults({
                @SortDefault(sort = "id", direction = Sort.Direction.ASC)})
			Pageable pageable) {
		return ResponseEntity.ok(developersService.findAll(pageable));
	}
	
	@PostMapping(path = WebConstants.DEVELOPER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> create(@Valid @RequestBody DeveloperReqPost developerReqPost) {
		return ResponseEntity.ok(developersService.create(developerReqPost));
	}
	
	@DeleteMapping(path = WebConstants.DEVELOPER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> deleteById(@PathVariable Long id) {
		return ResponseEntity.ok(developersService.deleteById(id));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.DEVELOPER + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> deleteByName(@PathVariable String name) {
		return ResponseEntity.ok(developersService.deleteByName(name));
	}
	
	@PatchMapping(path = WebConstants.DEVELOPER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> updateById(
			@PathVariable Long id,
			@Valid @RequestBody DeveloperReqPatch developerReqPatch) {
		return ResponseEntity.ok(developersService.updateById(id, developerReqPatch));
	}
}

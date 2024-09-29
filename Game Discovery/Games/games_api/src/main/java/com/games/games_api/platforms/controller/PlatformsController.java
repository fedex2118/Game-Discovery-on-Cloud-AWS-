package com.games.games_api.platforms.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.platforms.dto.PlatformReqPatch;
import com.games.games_api.platforms.dto.PlatformReqPost;
import com.games.games_api.platforms.dto.PlatformResp;
import com.games.games_api.platforms.service.IPlatformsService;
import com.games.games_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Platforms")
@RequestMapping(path = WebConstants.API_V1)
public class PlatformsController {

	@Autowired
	private IPlatformsService platformsService;
	
	@GetMapping(path = WebConstants.PLATFORM + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(platformsService.findById(id));
	}
	
	@GetMapping(path = WebConstants.PLATFORM + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> findByName(@PathVariable String name) {
		return ResponseEntity.ok(platformsService.findByName(name));
	}
	
	@GetMapping(path = WebConstants.PLATFORMS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> findAll(
			@ParameterObject @PageableDefault(size = 10) 
			@SortDefaults({
                @SortDefault(sort = "id", direction = Sort.Direction.ASC)})
			Pageable pageable) {
		return ResponseEntity.ok(platformsService.findAll(pageable));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = WebConstants.PLATFORM, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> create(@Valid @RequestBody PlatformReqPost platformReqPost) {
		return ResponseEntity.ok(platformsService.create(platformReqPost));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.PLATFORM + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> deleteById(@PathVariable Long id) {
		return ResponseEntity.ok(platformsService.deleteById(id));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.PLATFORM + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> deleteByName(@RequestParam String name) {
		return ResponseEntity.ok(platformsService.deleteByName(name));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.PLATFORM + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> updateById(
			@PathVariable Long id,
			@Valid @RequestBody PlatformReqPatch platformReqPatch) {
		return ResponseEntity.ok(platformsService.updateById(id, platformReqPatch));
	}
}

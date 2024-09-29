package com.users.user_preferences_api.platforms.controller;

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
import com.users.user_preferences_api.platforms.dto.PlatformReqPost;
import com.users.user_preferences_api.platforms.dto.PlatformResp;
import com.users.user_preferences_api.platforms.service.IPlatformsService;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Platforms")
@RequestMapping(path = WebConstants.API_V1)
public class PlatformsController {

	@Autowired
	private IPlatformsService platformsService;

	@PostMapping(path = WebConstants.PLATFORM, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> create(
			@Valid @RequestBody PlatformReqPost platformReqPost) {
		return ResponseEntity.ok(platformsService.create(platformReqPost));
	}
	
	@GetMapping(path = WebConstants.PLATFORM + "/{platformId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> findByKey(
			@PathVariable Long platformId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(platformsService.findByKey(platformId, userId));
	}
	
	@GetMapping(path = WebConstants.PLATFORM + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> findAllByUserId(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(platformsService.findAllByUserId(userId, pageable));
	}
	
	@DeleteMapping(path = WebConstants.PLATFORM + "/{platformId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> deleteByKey(
			@PathVariable Long platformId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(platformsService.deleteByKey(platformId, userId));
	}
	
	@DeleteMapping(path = WebConstants.PLATFORMS + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> deleteUserPlatforms(
			@PathVariable Long userId) {
		return ResponseEntity.ok(platformsService.deleteUserPlatforms(userId));
	}
	
	@DeleteMapping(path = WebConstants.PLATFORMS + "/{platformId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PlatformResp>> deletePlatforms(
			@PathVariable Long platformId) {
		return ResponseEntity.ok(platformsService.deletePlatforms(platformId));
	}
}

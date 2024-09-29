package com.users.user_preferences_api.developers.controller;

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
import com.users.user_preferences_api.developers.dto.DeveloperReqPost;
import com.users.user_preferences_api.developers.dto.DeveloperResp;
import com.users.user_preferences_api.developers.service.IDevelopersService;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Developers")
@RequestMapping(path = WebConstants.API_V1)
public class DevelopersController {

	@Autowired
	private IDevelopersService developersService;

	@PostMapping(path = WebConstants.DEVELOPER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> create(
			@Valid @RequestBody DeveloperReqPost developerReqPost) {
		return ResponseEntity.ok(developersService.create(developerReqPost));
	}
	
	@GetMapping(path = WebConstants.DEVELOPER + "/{developerId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> findByKey(
			@PathVariable Long developerId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(developersService.findByKey(developerId, userId));
	}
	
	@GetMapping(path = WebConstants.DEVELOPER + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> findAllByUserId(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(developersService.findAllByUserId(userId, pageable));
	}
	
	@DeleteMapping(path = WebConstants.DEVELOPER + "/{developerId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> deleteByKey(
			@PathVariable Long developerId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(developersService.deleteByKey(developerId, userId));
	}
	
	@DeleteMapping(path = WebConstants.DEVELOPERS + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> deleteUserDevelopers(
			@PathVariable Long userId) {
		return ResponseEntity.ok(developersService.deleteUserDevelopers(userId));
	}
	
	@DeleteMapping(path = WebConstants.DEVELOPERS + "/{developerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> deleteDevelopers(
			@PathVariable Long developerId) {
		return ResponseEntity.ok(developersService.deleteDevelopers(developerId));
	}
}

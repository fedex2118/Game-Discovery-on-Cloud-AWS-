package com.users.user_preferences_api.created_developers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperReqPost;
import com.users.user_preferences_api.created_developers.dto.CreatedDeveloperResp;
import com.users.user_preferences_api.created_developers.service.ICreatedDevelopersService;
import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Created Developers")
@RequestMapping(path = WebConstants.API_V1)
public class CreatedDevelopersController {

	@Autowired
	private ICreatedDevelopersService createdDevelopersService;

	@PostMapping(path = WebConstants.CREATED + WebConstants.DEVELOPERS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedDeveloperResp>> create(
			@Valid @RequestBody CreatedDeveloperReqPost createdDeveloperReqPost) {
		return ResponseEntity.ok(createdDevelopersService.create(createdDeveloperReqPost));
	}
	
	@PutMapping(path = WebConstants.CREATED + WebConstants.DEVELOPERS + "/{developerId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedDeveloperResp>> update(
			@PathVariable Long developerId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdDevelopersService.update(developerId, userId));
	}
	
	@GetMapping(path = WebConstants.CREATED + WebConstants.DEVELOPERS + "/{developerId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedDeveloperResp>> findByKey(
			@PathVariable Long developerId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdDevelopersService.findByKey(developerId, userId));
	}
	
	@GetMapping(path = WebConstants.CREATED + WebConstants.DEVELOPERS + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedDeveloperResp>> findAllByUserId(
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdDevelopersService.findByUserId(userId));
	}
	
	@DeleteMapping(path = WebConstants.CREATED + WebConstants.DEVELOPERS + "/{developerId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedDeveloperResp>> deleteByKey(
			@PathVariable Long developerId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdDevelopersService.deleteByKey(developerId, userId));
	}

}

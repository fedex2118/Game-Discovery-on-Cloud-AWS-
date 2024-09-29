package com.users.user_preferences_api.created_publishers.controller;

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

import com.users.user_preferences_api.created_publishers.dto.CreatedPublisherReqPost;
import com.users.user_preferences_api.created_publishers.dto.CreatedPublisherResp;
import com.users.user_preferences_api.created_publishers.service.ICreatedPublishersService;
import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Created Publishers")
@RequestMapping(path = WebConstants.API_V1)
public class CreatedPublishersController {

	@Autowired
	private ICreatedPublishersService createdPublishersService;

	@PostMapping(path = WebConstants.CREATED + WebConstants.PUBLISHERS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedPublisherResp>> create(
			@Valid @RequestBody CreatedPublisherReqPost createdPublisherReqPost) {
		return ResponseEntity.ok(createdPublishersService.create(createdPublisherReqPost));
	}
	
	@PutMapping(path = WebConstants.CREATED + WebConstants.PUBLISHERS + "/{publisherId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedPublisherResp>> update(
			@PathVariable Long publisherId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdPublishersService.update(publisherId, userId));
	}
	
	@GetMapping(path = WebConstants.CREATED + WebConstants.PUBLISHERS + "/{publisherId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedPublisherResp>> findByKey(
			@PathVariable Long publisherId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdPublishersService.findByKey(publisherId, userId));
	}
	
	@DeleteMapping(path = WebConstants.CREATED + WebConstants.PUBLISHERS + "/{publisherId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<CreatedPublisherResp>> deleteByKey(
			@PathVariable Long publisherId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(createdPublishersService.deleteByKey(publisherId, userId));
	}

}

package com.users.user_preferences_api.publishers.controller;

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
import com.users.user_preferences_api.publishers.dto.PublisherReqPost;
import com.users.user_preferences_api.publishers.dto.PublisherResp;
import com.users.user_preferences_api.publishers.service.IPublishersService;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Publishers")
@RequestMapping(path = WebConstants.API_V1)
public class PublishersController {

	@Autowired
	private IPublishersService publishersService;

	@PostMapping(path = WebConstants.PUBLISHER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> create(
			@Valid @RequestBody PublisherReqPost publisherReqPost) {
		return ResponseEntity.ok(publishersService.create(publisherReqPost));
	}
	
	@GetMapping(path = WebConstants.PUBLISHER + "/{publisherId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> findByKey(
			@PathVariable Long publisherId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(publishersService.findByKey(publisherId, userId));
	}
	
	@GetMapping(path = WebConstants.PUBLISHER + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> findAllByUserId(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(publishersService.findAllByUserId(userId, pageable));
	}
	
	@DeleteMapping(path = WebConstants.PUBLISHER + "/{publisherId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> deleteByKey(
			@PathVariable Long publisherId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(publishersService.deleteByKey(publisherId, userId));
	}
	
	@DeleteMapping(path = WebConstants.PUBLISHERS + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> deleteUserPublishers(
			@PathVariable Long userId) {
		return ResponseEntity.ok(publishersService.deleteUserPublishers(userId));
	}
	
	@DeleteMapping(path = WebConstants.PUBLISHERS + "/{publisherId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> deletePublishers(
			@PathVariable Long publisherId) {
		return ResponseEntity.ok(publishersService.deletePublishers(publisherId));
	}
}

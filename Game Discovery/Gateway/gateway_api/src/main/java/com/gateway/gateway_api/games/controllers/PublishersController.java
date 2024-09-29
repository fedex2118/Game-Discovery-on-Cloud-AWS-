package com.gateway.gateway_api.games.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.games.data.classes.PublisherReqPatch;
import com.gateway.gateway_api.games.data.classes.PublisherReqPost;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.games.services.IPublishersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Publishers")
@RequestMapping(WebConstants.API_V1)
public class PublishersController {

	@Autowired
	private IPublishersService publishersService;
	
	@PostMapping(path = WebConstants.PUBLISHER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> create(@Valid @RequestBody PublisherReqPost publisherReqPost) {
		return ResponseEntity.ok(publishersService.create(publisherReqPost));
	}
	
	@PatchMapping(path = WebConstants.PUBLISHER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> updateById(
			@PathVariable Long id,
			@Valid @RequestBody PublisherReqPatch publisherReqPatch) {
		return ResponseEntity.ok(publishersService.updateById(id, publisherReqPatch));
	}
	
	@GetMapping(path = WebConstants.PUBLISHER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(publishersService.findById(id));
	}
	
	@GetMapping(path = WebConstants.PUBLISHER + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> findByName(@PathVariable String name) {
		return ResponseEntity.ok(publishersService.findByName(name));
	}
}

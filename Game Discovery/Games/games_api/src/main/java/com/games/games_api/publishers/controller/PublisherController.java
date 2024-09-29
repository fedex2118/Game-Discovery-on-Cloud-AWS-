package com.games.games_api.publishers.controller;

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
import com.games.games_api.publishers.dto.PublisherReqPatch;
import com.games.games_api.publishers.dto.PublisherReqPost;
import com.games.games_api.publishers.dto.PublisherResp;
import com.games.games_api.publishers.service.IPublishersService;
import com.games.games_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Publishers")
@RequestMapping(path = WebConstants.API_V1)
public class PublisherController {

	@Autowired
	private IPublishersService publishersService;
	
	@GetMapping(path = WebConstants.PUBLISHER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(publishersService.findById(id));
	}
	
	@GetMapping(path = WebConstants.PUBLISHER + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> findByName(@PathVariable String name) {
		return ResponseEntity.ok(publishersService.findByName(name));
	}
	
	@GetMapping(path = WebConstants.PUBLISHERS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> findAll(
			@ParameterObject @PageableDefault(size = 10) 
			@SortDefaults({
                @SortDefault(sort = "id", direction = Sort.Direction.ASC)})
			Pageable pageable) {
		return ResponseEntity.ok(publishersService.findAll(pageable));
	}
	
	@PostMapping(path = WebConstants.PUBLISHER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> create(@Valid @RequestBody PublisherReqPost publisherReqPost) {
		return ResponseEntity.ok(publishersService.create(publisherReqPost));
	}
	
	@DeleteMapping(path = WebConstants.PUBLISHER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> deleteById(@PathVariable Long id) {
		return ResponseEntity.ok(publishersService.deleteById(id));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.PUBLISHER + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> deleteByName(@PathVariable String name) {
		return ResponseEntity.ok(publishersService.deleteByName(name));
	}
	
	@PatchMapping(path = WebConstants.PUBLISHER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> updateById(
			@PathVariable Long id,
			@Valid @RequestBody PublisherReqPatch publisherReqPatch) {
		return ResponseEntity.ok(publishersService.updateById(id, publisherReqPatch));
	}
}

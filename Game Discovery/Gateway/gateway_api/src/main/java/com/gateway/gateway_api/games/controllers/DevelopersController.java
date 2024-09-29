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
import com.gateway.gateway_api.games.data.classes.DeveloperReqPatch;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPost;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.services.IDevelopersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Developers")
@RequestMapping(WebConstants.API_V1)
public class DevelopersController {

	@Autowired
	private IDevelopersService developersService;
	
	@PostMapping(path = WebConstants.DEVELOPER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> create(@Valid @RequestBody DeveloperReqPost developerReqPost) {
		return ResponseEntity.ok(developersService.create(developerReqPost));
	}
	
	@PatchMapping(path = WebConstants.DEVELOPER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> updateById(
			@PathVariable Long id,
			@Valid @RequestBody DeveloperReqPatch developerReqPatch) {
		return ResponseEntity.ok(developersService.updateById(id, developerReqPatch));
	}
	
	@GetMapping(path = WebConstants.DEVELOPER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(developersService.findById(id));
	}
	
	@GetMapping(path = WebConstants.DEVELOPER + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> findByName(@PathVariable String name) {
		return ResponseEntity.ok(developersService.findByName(name));
	}
}

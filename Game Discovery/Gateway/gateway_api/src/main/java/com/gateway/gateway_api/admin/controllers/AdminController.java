package com.gateway.gateway_api.admin.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.admin.services.IAdminService;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPatch;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPost;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.GameReqPatchStatus;
import com.gateway.gateway_api.games.data.classes.GameReqPut;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GameRespTimestamp;
import com.gateway.gateway_api.games.data.classes.GameStatusResp;
import com.gateway.gateway_api.games.data.classes.PublisherReqPatch;
import com.gateway.gateway_api.games.data.classes.PublisherReqPost;
import com.gateway.gateway_api.games.data.classes.PublisherResp;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Admin")
@RequestMapping(WebConstants.API_V1 + "/by-admin")
public class AdminController {
	
	@Autowired
	private IAdminService adminService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.GAMES + "/synch-with-reviews/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<Long>> synchAllGamesWithReviews() {
		return ResponseEntity.ok(adminService.synchAllGamesWithReviews());
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.GAMES + "/synch-with-reviews", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<Long>> synchInputGamesWithReviews(@RequestBody List<Long> gameIds) {
		return ResponseEntity.ok(adminService.synchInputGamesWithReviews(gameIds));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = WebConstants.GAMES + "/with-pending-status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameRespTimestamp>> findAllGamesOnPending(
			@ParameterObject @PageableDefault(size = 10)
			@SortDefaults({
                @SortDefault(sort = "timestamp", direction = Sort.Direction.ASC)
        }) Pageable pageable) {
		return ResponseEntity.ok(adminService.findAllGamesOnPending(pageable));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.GAMES + "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameStatusResp>> patchStatus(
			@Valid @RequestBody List<GameReqPatchStatus> gameReqPatchList) {
		return ResponseEntity.ok(adminService.patchStatus(gameReqPatchList));
	}
	
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('GATEWAY')")
	@DeleteMapping(path = WebConstants.GAMES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> deleteAllGamesByIds(
			@RequestParam List<Long> gameIds) {
		return ResponseEntity.ok(adminService.deleteAllGamesByIds(gameIds));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = WebConstants.DEVELOPER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> createDeveloper(@Valid @RequestBody DeveloperReqPost developerReqPost) {
		return ResponseEntity.ok(adminService.createDeveloper(developerReqPost));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = WebConstants.PUBLISHER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> createPublisher(@Valid @RequestBody PublisherReqPost publisherReqPost) {
		return ResponseEntity.ok(adminService.createPublisher(publisherReqPost));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.DEVELOPER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<DeveloperResp>> updateDeveloperById(
			@PathVariable Long id,
			@Valid @RequestBody DeveloperReqPatch developerReqPatch) {
		return ResponseEntity.ok(adminService.updateDeveloperById(id, developerReqPatch));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.PUBLISHER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PublisherResp>> updatePublisherById(
			@PathVariable Long id,
			@Valid @RequestBody PublisherReqPatch publisherReqPatch) {
		return ResponseEntity.ok(adminService.updatePublisherById(id, publisherReqPatch));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(path = WebConstants.GAME + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> updateGame(
			@PathVariable Long id,
			@Valid @RequestBody GameReqPut gameReqPut) {
		return ResponseEntity.ok(adminService.updateGame(id, gameReqPut));
	}
	
	
}

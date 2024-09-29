package com.gateway.gateway_api.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.users.data.classes.UserReqPatch;
import com.gateway.gateway_api.users.data.classes.UserResp;
import com.gateway.gateway_api.users.services.IUsersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(WebConstants.API_V1 + WebConstants.USER)
@Tag(name = "Users")
public class UsersController {

	@Autowired
	private IUsersService usersService;
	
	@GetMapping(path = "/user-id", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UserResp>> getId() {
		return ResponseEntity.ok(usersService.getId());
	}
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UserResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(usersService.findById(id));
	}
	
	@PatchMapping(path = "/{id}")
	public ResponseEntity<CollectionResponse<UserResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody UserReqPatch userReqPatch) {
		return ResponseEntity.ok(usersService.updateById(id, userReqPatch));
	}
	
	@DeleteMapping()
	public ResponseEntity<CollectionResponse<UserResp>> delete(
			@RequestParam String email) {
		return ResponseEntity.ok(usersService.deleteUserByEmail(email));
	}
}

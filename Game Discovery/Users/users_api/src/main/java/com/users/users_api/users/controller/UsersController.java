package com.users.users_api.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.custom.view.UserRespView;
import com.users.users_api.users.dto.UserReqPatch;
import com.users.users_api.users.dto.UserReqPost;
import com.users.users_api.users.dto.UserResp;
import com.users.users_api.users.service.IUsersService;
import com.users.users_api.utils.Role;
import com.users.users_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(WebConstants.API_V1)
@Tag(name = "Users")
public class UsersController {
	
	@Autowired
	private IUsersService usersService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@JsonView(UserRespView.Post.class)
	@PostMapping(path = WebConstants.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UserResp>> create(@Valid @RequestBody UserReqPost userReq, Role role) {
		return ResponseEntity.ok(usersService.create(userReq, role));
	}
	
    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(UserRespView.Post.class)
    @GetMapping(path = WebConstants.USERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse<UserResp>> findAll() {
    	CollectionResponse<UserResp> users = usersService.findAll();
        return ResponseEntity.ok(users);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @JsonView(UserRespView.GetEmail.class)
	@GetMapping(path = WebConstants.USER + "/{id}/email", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UserResp>> getEmail(@PathVariable Long id) {
		return ResponseEntity.ok(usersService.getEmail(id));
	}
    
	@PreAuthorize("hasAuthority('ADMIN')")
	@JsonView(UserRespView.All.class)
	@DeleteMapping(path = WebConstants.USER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<UserResp>> deleteById(@PathVariable Long id) {
		return new ResponseEntity<>(usersService.deleteById(id), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping(path = WebConstants.USER, produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(UserRespView.All.class)
	public ResponseEntity<CollectionResponse<UserResp>> deleteByEmail(HttpServletRequest request, 
			@RequestParam String email) {
		return new ResponseEntity<>(usersService.deleteByEmail(request, email), HttpStatus.ACCEPTED);
	}
	
	@GetMapping(path = WebConstants.USER + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(UserRespView.Get.class)
	public ResponseEntity<CollectionResponse<UserResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(usersService.findById(id));
	}
	
	@GetMapping(path = WebConstants.USER + "/id", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(UserRespView.GetId.class)
	public ResponseEntity<CollectionResponse<UserResp>> getId(@RequestParam String email) {
		return ResponseEntity.ok(usersService.getId(email));
	}
	
	@PatchMapping(path = WebConstants.USER + "/{id}")
	@JsonView(UserRespView.Patch.class)
	public ResponseEntity<CollectionResponse<UserResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody UserReqPatch userReqPatch) {
		return ResponseEntity.ok(usersService.updateById(id, userReqPatch));
	}
	
}

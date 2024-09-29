package com.users.users_api.roles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.roles.dto.RoleResp;
import com.users.users_api.roles.service.IRolesService;
import com.users.users_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(WebConstants.API_V1 + WebConstants.ROLE)
@Tag(name = "Roles")
public class RolesController {
	
	@Autowired
	private IRolesService rolesService;

	@GetMapping(path = "/{id}")
	public ResponseEntity<CollectionResponse<RoleResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(rolesService.findById(id));
	}
}

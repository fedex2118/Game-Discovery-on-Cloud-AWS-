package com.gateway.gateway_api.users.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.users.data.classes.AuthResp;
import com.gateway.gateway_api.users.services.IAuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(WebConstants.API_V1 + WebConstants.TOKEN)
@Tag(name = "Token")
public class TokenController {
	
	@Autowired
	private IAuthService authService;

	@PostMapping(path = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<AuthResp>> refreshToken()
			throws IOException {
		CollectionResponse<AuthResp> authResp = authService.refreshToken();
		return ResponseEntity.ok(authResp);
	}
}

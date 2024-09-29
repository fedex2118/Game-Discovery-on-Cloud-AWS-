package com.gateway.gateway_api.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.users.data.classes.AuthReq;
import com.gateway.gateway_api.users.data.classes.AuthResp;
import com.gateway.gateway_api.users.request.dto.UserSignUpPost;
import com.gateway.gateway_api.users.services.IAuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(WebConstants.API_V1 + WebConstants.AUTH)
@Tag(name = "Authorization")
public class AuthController {
	
	@Autowired
	private IAuthService authService;
	
	@PostMapping(path = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<AuthResp>> signUp(@Valid @RequestBody UserSignUpPost userReq) {
		return ResponseEntity.ok(authService.signUp(userReq));
	}

	@PostMapping(path = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<AuthResp>>authenticate(@Valid @RequestBody AuthReq authReq) {
		return ResponseEntity.ok(authService.authenticate(authReq));
	}
}

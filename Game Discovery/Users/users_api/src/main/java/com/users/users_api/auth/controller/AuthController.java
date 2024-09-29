package com.users.users_api.auth.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.users_api.auth.dto.AuthReq;
import com.users.users_api.auth.dto.AuthResp;
import com.users.users_api.auth.service.IAuthService;
import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.users.dto.UserReqPost;
import com.users.users_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(WebConstants.API_V1 + WebConstants.AUTH)
@Tag(name = "Authorization")
public class AuthController {

	@Autowired
	private IAuthService authService;

	@PostMapping(path = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<AuthResp>> signUp(@Valid @RequestBody UserReqPost userReq) {
		return ResponseEntity.ok(authService.signUp(userReq));
	}

	@PostMapping(path = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<AuthResp>>authenticate(@Valid @RequestBody AuthReq authReq) {
		return ResponseEntity.ok(authService.authenticate(authReq));
	}

	@PostMapping(path = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<AuthResp>> refreshToken(HttpServletRequest request)
			throws IOException {
		CollectionResponse<AuthResp> authResp = authService.refreshToken(request);
		return ResponseEntity.ok(authResp);
	}
}

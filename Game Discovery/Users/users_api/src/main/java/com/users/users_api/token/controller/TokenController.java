package com.users.users_api.token.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.token.dto.TokenResp;
import com.users.users_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(WebConstants.API_V1 + WebConstants.TOKEN)
@Tag(name = "TokenValidation")
public class TokenController {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

	@GetMapping(path = "/valid", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<TokenResp>> isTokenValid() {
		return ResponseEntity.ok(new CollectionResponse<>(new TokenResp(true)));
	}
}

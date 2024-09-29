package com.gateway.gateway_api.discovery.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.discovery.services.IGameDiscoveryService;
import com.gateway.gateway_api.games.data.classes.GameResp;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Games Discovery")
@RequestMapping(WebConstants.API_V1 + WebConstants.DISCOVERY)
public class GameDiscoveryController {

	@Autowired
	private IGameDiscoveryService gameDiscoveryService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GameResp>> discover(
			@Parameter(description = "A value in the range [0, 1] that adds randomness to the discovery."
					+ "<br>If no value is passed then it will have a default value of 0.5. Passing 1 means that the search engine will look only to your"
					+ "<br>preferences and not look for anything else."
					+ "<br>Note that if you don't have any preference yet or if you put 'notRandom=0' the search will be completely random."
					+ "<br>The games you own will be automatically excluded from the results."
					) @RequestParam(defaultValue = "0.5") Double notRandom,
			@ParameterObject @PageableDefault(size = 10)
			@SortDefaults({
                @SortDefault(sort = "name", direction = Sort.Direction.ASC),
                @SortDefault(sort = "releaseDate", direction = Sort.Direction.DESC),
                @SortDefault(sort = "price", direction = Sort.Direction.ASC)
        }) Pageable pageable
			) {
		return ResponseEntity.ok(gameDiscoveryService.discover(notRandom));
	}
}

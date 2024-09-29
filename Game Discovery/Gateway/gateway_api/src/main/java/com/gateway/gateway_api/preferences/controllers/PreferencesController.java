package com.gateway.gateway_api.preferences.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPost;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPut;
import com.gateway.gateway_api.preferences.services.IPreferencesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(WebConstants.API_V1 + WebConstants.PREFERENCE)
@Tag(name = "User Preferences")
public class PreferencesController {
	
	@Autowired
	private IPreferencesService preferencesService;

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PreferenceResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(preferencesService.findById(id));
	}

	@PostMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PreferenceResp>> create(
			@PathVariable Long id,
			@Valid @RequestBody PreferencesPost preferencesPost) {
		return ResponseEntity.ok(preferencesService.create(id, preferencesPost));
	}
	
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PreferenceResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody PreferencesPut preferencesPut) {
		return ResponseEntity.ok(preferencesService.update(id, preferencesPut));
	}
	
//	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<CollectionResponse<PreferenceResp>> deleteById(
//			@PathVariable Long id) {
//		return ResponseEntity.ok(preferencesService.deleteById(id));
//	}
}

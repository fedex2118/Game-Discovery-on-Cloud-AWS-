package com.users.user_preferences_api.preferences.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPost;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPut;
import com.users.user_preferences_api.preferences.dto.PreferenceResp;
import com.users.user_preferences_api.preferences.service.IPreferencesService;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Preferences")
@RequestMapping(path = WebConstants.API_V1 + WebConstants.PREFERENCE)
public class PreferencesController {
	
	@Autowired
	private IPreferencesService preferencesService;
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PreferenceResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(preferencesService.findById(id));
	}
	
	@GetMapping(path = "/userId/by-gameId/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<Long>> findIdByOwnedGame(@PathVariable Long gameId) {
		return ResponseEntity.ok(preferencesService.findUserIdByOwnedGame(gameId));
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PreferenceResp>> create(
			@Valid @RequestBody PreferenceReqPost preferenceReqPost) {
		return ResponseEntity.ok(preferencesService.create(preferenceReqPost));
	}
	
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PreferenceResp>> update(
			@PathVariable Long id,
			@Valid @RequestBody PreferenceReqPut preferenceReqPut) {
		return ResponseEntity.ok(preferencesService.update(id, preferenceReqPut));
	}
	
	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<PreferenceResp>> deleteById(
			@PathVariable Long id) {
		return ResponseEntity.ok(preferencesService.deleteById(id));
	}
	
	
}

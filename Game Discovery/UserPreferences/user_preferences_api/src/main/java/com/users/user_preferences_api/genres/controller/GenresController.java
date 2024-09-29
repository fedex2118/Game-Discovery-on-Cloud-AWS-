package com.users.user_preferences_api.genres.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.genres.dto.GenreReqPost;
import com.users.user_preferences_api.genres.dto.GenreResp;
import com.users.user_preferences_api.genres.service.IGenresService;
import com.users.user_preferences_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "User Genres")
@RequestMapping(path = WebConstants.API_V1)
public class GenresController {

	@Autowired
	private IGenresService genresService;

	@PostMapping(path = WebConstants.GENRE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> create(
			@Valid @RequestBody GenreReqPost genreReqPost) {
		return ResponseEntity.ok(genresService.create(genreReqPost));
	}
	
	@GetMapping(path = WebConstants.GENRE + "/{genreId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> findByKey(
			@PathVariable Long genreId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(genresService.findByKey(genreId, userId));
	}
	
	@GetMapping(path = WebConstants.GENRE + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> findAllByUserId(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(genresService.findAllByUserId(userId, pageable));
	}
	
	@DeleteMapping(path = WebConstants.GENRE + "/{genreId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> deleteByKey(
			@PathVariable Long genreId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(genresService.deleteByKey(genreId, userId));
	}
	
	@DeleteMapping(path = WebConstants.GENRES + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> deleteUserGenres(
			@PathVariable Long userId) {
		return ResponseEntity.ok(genresService.deleteUserGenres(userId));
	}
	
	@DeleteMapping(path = WebConstants.GENRES + "/{genreId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> deleteGenres(
			@PathVariable Long genreId) {
		return ResponseEntity.ok(genresService.deleteGenres(genreId));
	}
}

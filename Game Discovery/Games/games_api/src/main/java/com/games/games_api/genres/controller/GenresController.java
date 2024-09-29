package com.games.games_api.genres.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
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
import org.springframework.web.bind.annotation.RestController;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.genres.dto.GenreReqPatch;
import com.games.games_api.genres.dto.GenreReqPost;
import com.games.games_api.genres.dto.GenreResp;
import com.games.games_api.genres.service.IGenresService;
import com.games.games_api.utils.WebConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Genres")
@RequestMapping(path = WebConstants.API_V1)
public class GenresController {

	@Autowired
	private IGenresService genresService;
	
	@GetMapping(path = WebConstants.GENRE + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(genresService.findById(id));
	}
	
	@GetMapping(path = WebConstants.GENRE + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> findByName(@PathVariable String name) {
		return ResponseEntity.ok(genresService.findByName(name));
	}
	
	@GetMapping(path = WebConstants.GENRES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> findAll(
			@ParameterObject @PageableDefault(size = 10) 
			@SortDefaults({
                @SortDefault(sort = "id", direction = Sort.Direction.ASC)})
			Pageable pageable) {
		return ResponseEntity.ok(genresService.findAll(pageable));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = WebConstants.GENRE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> create(@Valid @RequestBody GenreReqPost genreReqPost) {
		return ResponseEntity.ok(genresService.create(genreReqPost));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.GENRE + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> deleteById(@PathVariable Long id) {
		return ResponseEntity.ok(genresService.deleteById(id));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.GENRE + "/by-name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> deleteByName(@PathVariable String name) {
		return ResponseEntity.ok(genresService.deleteByName(name));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PatchMapping(path = WebConstants.GENRE + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<GenreResp>> updateById(
			@PathVariable Long id,
			@Valid @RequestBody GenreReqPatch genreReqPatch) {
		return ResponseEntity.ok(genresService.updateById(id, genreReqPatch));
	}
}

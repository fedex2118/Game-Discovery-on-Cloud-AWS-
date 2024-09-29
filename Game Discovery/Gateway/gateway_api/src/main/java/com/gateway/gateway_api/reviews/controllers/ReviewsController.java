package com.gateway.gateway_api.reviews.controllers;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.WebConstants;
import com.gateway.gateway_api.reviews.data.classes.ReviewCriteria;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPost;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPut;
import com.gateway.gateway_api.reviews.data.classes.ReviewResp;
import com.gateway.gateway_api.reviews.data.classes.ReviewUpdateResp;
import com.gateway.gateway_api.reviews.services.IReviewsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Game Reviews")
@RequestMapping(path = WebConstants.API_V1)
public class ReviewsController {

	@Autowired
	private IReviewsService reviewsService;

	@PostMapping(path = WebConstants.REVIEW + WebConstants.GAME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> create(
			@Valid @RequestBody ReviewReqPost reviewReqPost) {
		return ResponseEntity.ok(reviewsService.create(reviewReqPost));
	}
	
	@PutMapping(path = WebConstants.REVIEW + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewUpdateResp>> update(
			@PathVariable Long gameId,
			@PathVariable Long userId,
			@Valid @RequestBody ReviewReqPut reviewReqPut) {
		return ResponseEntity.ok(reviewsService.update(gameId, userId, reviewReqPut));
	}
	
	@GetMapping(path = WebConstants.REVIEW + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> findByKey(
			@PathVariable Long gameId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(reviewsService.findByKey(gameId, userId));
	}
	
	@GetMapping(path = WebConstants.REVIEW + WebConstants.GAME + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> findAllByUserId(
			@PathVariable Long userId,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(reviewsService.findAllByUserId(userId, pageable));
	}
	
	@GetMapping(path = WebConstants.REVIEWS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> findByCriteria(
			@Valid @ParameterObject ReviewCriteria reviewCriteria,
			@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(reviewsService.findByCriteria(reviewCriteria, pageable));
	}
	
	@DeleteMapping(path = WebConstants.REVIEW + WebConstants.GAME + "/{gameId}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> deleteByKey(
			@PathVariable Long gameId,
			@PathVariable Long userId) {
		return ResponseEntity.ok(reviewsService.deleteByKey(gameId, userId));
	}
}
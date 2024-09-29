package com.reviews.reviews_api.reviews.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reviews.reviews_api.custom.response.CollectionResponse;
import com.reviews.reviews_api.custom.utils.WebConstants;
import com.reviews.reviews_api.reviews.dto.ReviewCriteria;
import com.reviews.reviews_api.reviews.dto.ReviewReqPost;
import com.reviews.reviews_api.reviews.dto.ReviewReqPut;
import com.reviews.reviews_api.reviews.dto.ReviewResp;
import com.reviews.reviews_api.reviews.dto.ReviewRespAvgRating;
import com.reviews.reviews_api.reviews.dto.ReviewRespCount;
import com.reviews.reviews_api.reviews.dto.ReviewUpdateResp;
import com.reviews.reviews_api.reviews.service.IReviewsService;

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
	
	@GetMapping(path = WebConstants.REVIEW + WebConstants.GAME + "/{gameId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewRespCount>> retrieveCount(
			@PathVariable Long gameId) {
		return ResponseEntity.ok(reviewsService.retrieveCount(gameId));
	}
	
	@GetMapping(path = WebConstants.REVIEW + WebConstants.GAME + "/{gameId}/average-rating", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewRespAvgRating>> retrieveAvgRating(
			@PathVariable Long gameId) {
		return ResponseEntity.ok(reviewsService.retrieveAvgRating(gameId));
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
	
	@DeleteMapping(path = WebConstants.REVIEW + WebConstants.GAMES + "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> deleteUserReviews(
			@PathVariable Long userId) {
		return ResponseEntity.ok(reviewsService.deleteUserReviews(userId));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(path = WebConstants.REVIEW + WebConstants.GAMES + "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> deleteGameReviews(
			@PathVariable Long gameId) {
		return ResponseEntity.ok(reviewsService.deleteGameReviews(gameId));
	}
	
	@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('GATEWAY')")
	@DeleteMapping(path = WebConstants.REVIEW + WebConstants.GAMES + "/by-gameIds", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResponse<ReviewResp>> deleteAllReviewByGameIds(
			@RequestParam List<Long> gameIds) {
		return ResponseEntity.ok(reviewsService.deleteAllReviewsByGameIds(gameIds));
	}
}

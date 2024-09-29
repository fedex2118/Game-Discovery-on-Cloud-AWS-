package com.gateway.gateway_api.reviews.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.gateway.gateway_api.application.AppProperties;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.response.ResponseHandler;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPost;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPut;
import com.gateway.gateway_api.reviews.data.classes.ReviewResp;
import com.gateway.gateway_api.reviews.data.classes.ReviewRespAvgRating;
import com.gateway.gateway_api.reviews.data.classes.ReviewRespCount;
import com.gateway.gateway_api.reviews.data.classes.ReviewUpdateResp;

@Service
public class ReviewsRequesterService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewsRequesterService.class);

	private final WebClient webClient;
	private final String baseUrl;

	@Autowired
	public ReviewsRequesterService(WebClient.Builder webClientBuilder, AppProperties appProperties) {
		this.baseUrl = appProperties.getMicroservicesConfig().getReviewsBaseUrl();
		this.webClient = webClientBuilder.build();
	}

	public CollectionResponse<ReviewResp> create(ReviewReqPost request) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "game");
		ParameterizedTypeReference<CollectionResponse<ReviewResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewResp> response = this.webClient.post().uri(uriBuilder.build().toUri())
				.bodyValue(request)
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}

	public CollectionResponse<ReviewUpdateResp> update(Long gameId, Long userId, ReviewReqPut request) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "game", "{gameId}", "user", "{userId}");
		ParameterizedTypeReference<CollectionResponse<ReviewUpdateResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewUpdateResp> response = this.webClient.put()
				.uri(uriBuilder.build(gameId, userId))
				.bodyValue(request)
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewResp> delete(Long gameId, Long userId) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "game", "{gameId}", "user", "{userId}");
		ParameterizedTypeReference<CollectionResponse<ReviewResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewResp> response = this.webClient.delete()
				.uri(uriBuilder.build(gameId, userId))
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewResp> deleteAll(Long userId) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "games", "user", "{userId}");
		ParameterizedTypeReference<CollectionResponse<ReviewResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewResp> response = this.webClient.delete()
				.uri(uriBuilder.build(userId))
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewResp> findByKey(Long gameId, Long userId) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "game", "{gameId}", "user", "{userId}");
		ParameterizedTypeReference<CollectionResponse<ReviewResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewResp> response = this.webClient.get()
				.uri(uriBuilder.build(gameId, userId))
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewResp> findAllByUserId(Long userId,
			MultiValueMap<String, String> params) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "game", "user", "{userId}").queryParams(params);
		ParameterizedTypeReference<CollectionResponse<ReviewResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewResp> response = this.webClient.get()
				.uri(uriBuilder.build(userId))
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewResp> findByCriteria(MultiValueMap<String, String> params) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "reviews").queryParams(params);
		ParameterizedTypeReference<CollectionResponse<ReviewResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewResp> response = this.webClient.get()
				.uri(uriBuilder.build().toUri())
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewRespCount> getReviewQuantity(Long gameId) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "game", "{gameId}", "count");
		ParameterizedTypeReference<CollectionResponse<ReviewRespCount>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewRespCount> response = this.webClient.get()
				.uri(uriBuilder.build(gameId))
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewRespAvgRating> getReviewAvgRating(Long gameId) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "game", "{gameId}", "average-rating");
		ParameterizedTypeReference<CollectionResponse<ReviewRespAvgRating>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewRespAvgRating> response = this.webClient.get()
				.uri(uriBuilder.build(gameId))
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}
	
	public CollectionResponse<ReviewResp> deleteByGameIds(List<Long> gameIds) {
		List<String> ids = gameIds.stream()
				.map(el -> el.toString())
				.toList();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.addAll("gameIds", ids);
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.pathSegment("v1", "review", "games", "by-gameIds").queryParams(params);
		ParameterizedTypeReference<CollectionResponse<ReviewResp>> responseType = new ParameterizedTypeReference<>() {
		};

		CollectionResponse<ReviewResp> response = this.webClient.delete()
				.uri(uriBuilder.build().toUri())
				.retrieve()
				.bodyToMono(responseType)
				.onErrorResume(ResponseHandler::handleErrorResponse)
				.block();

		return response;
	}


}

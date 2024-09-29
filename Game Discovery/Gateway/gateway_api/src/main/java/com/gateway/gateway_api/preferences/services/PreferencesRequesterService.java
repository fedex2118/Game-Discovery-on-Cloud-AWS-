package com.gateway.gateway_api.preferences.services;

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
import com.gateway.gateway_api.preferences.data.classes.CreatedDeveloperReqPost;
import com.gateway.gateway_api.preferences.data.classes.CreatedDeveloperResp;
import com.gateway.gateway_api.preferences.data.classes.CreatedGameReqPost;
import com.gateway.gateway_api.preferences.data.classes.CreatedGameResp;
import com.gateway.gateway_api.preferences.data.classes.CreatedPublisherReqPost;
import com.gateway.gateway_api.preferences.data.classes.CreatedPublisherResp;
import com.gateway.gateway_api.preferences.data.classes.PreferenceReqPost;
import com.gateway.gateway_api.preferences.data.classes.PreferenceReqPut;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;

@Service
public class PreferencesRequesterService {
	
	private static final Logger logger = LoggerFactory.getLogger(PreferencesRequesterService.class);

    private final WebClient webClient;
    private final String baseUrl;

    @Autowired
    public PreferencesRequesterService(WebClient.Builder webClientBuilder,
    		AppProperties appProperties) {
    	this.baseUrl = appProperties.getMicroservicesConfig().getPreferencesBaseUrl();
        this.webClient = webClientBuilder
            .build();
    }
    
    public CollectionResponse<PreferenceResp> findById(Long id) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "preference", "{id}");
    	ParameterizedTypeReference<CollectionResponse<PreferenceResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PreferenceResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<Long> findUserIdByOwnedGame(Long gameId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "preference", "userId", "by-gameId", "{gameId}");
    	ParameterizedTypeReference<CollectionResponse<Long>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<Long> response = this.webClient.get()
            .uri(uriBuilder.build(gameId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<PreferenceResp> create(PreferenceReqPost request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "preference");
    	ParameterizedTypeReference<CollectionResponse<PreferenceResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PreferenceResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<PreferenceResp> update(Long id, PreferenceReqPut request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "preference", "{id}");
    	ParameterizedTypeReference<CollectionResponse<PreferenceResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PreferenceResp> response = this.webClient.put()
            .uri(uriBuilder.build(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<PreferenceResp> delete(Long id) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "preference", "{id}");
    	ParameterizedTypeReference<CollectionResponse<PreferenceResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PreferenceResp> response = this.webClient.delete()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedGameResp> findCreatedGameByKey(Long gameId, Long userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "game", "{gameId}", "user", "{userId}");
    	ParameterizedTypeReference<CollectionResponse<CreatedGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedGameResp> response = this.webClient.get()
            .uri(uriBuilder.build(gameId, userId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    
    public CollectionResponse<CreatedGameResp> findAllByUserIdLastFortyEightHours(Long userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "games", "user", "{userId}", "last-forty-eight-hours");
    	ParameterizedTypeReference<CollectionResponse<CreatedGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedGameResp> response = this.webClient.get()
            .uri(uriBuilder.build(userId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedGameResp> createUserCreatedGame(CreatedGameReqPost request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "game");
    	ParameterizedTypeReference<CollectionResponse<CreatedGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedGameResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedGameResp> updateUserCreatedGame(Long gameId, Long userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "game", "{gameId}" ,"user", "{userId}");
    	ParameterizedTypeReference<CollectionResponse<CreatedGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedGameResp> response = this.webClient.put()
            .uri(uriBuilder.build(gameId, userId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedGameResp> deleteGamesWithIds(List<Long> gameIds) {
		List<String> ids = gameIds.stream()
                .map(el -> String.valueOf(el))
                .toList();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.addAll("gameIds", ids);
    	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "games").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<CreatedGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedGameResp> response = this.webClient.delete()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedDeveloperResp> getCreatedDeveloper(Long developerId, Long userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "developers", "{developerId}", "user", "{userId}");
    	ParameterizedTypeReference<CollectionResponse<CreatedDeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedDeveloperResp> response = this.webClient.get()
            .uri(uriBuilder.build(developerId, userId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedDeveloperResp> findCreatedDeveloperByUserId(Long userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "developers", "user", "{userId}");
    	ParameterizedTypeReference<CollectionResponse<CreatedDeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedDeveloperResp> response = this.webClient.get()
            .uri(uriBuilder.build(userId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedDeveloperResp> createUserDeveloper(CreatedDeveloperReqPost request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "developers");
    	ParameterizedTypeReference<CollectionResponse<CreatedDeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedDeveloperResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedPublisherResp> findCreatedPublisherByUserId(Long userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "publishers", "user", "{userId}");
    	ParameterizedTypeReference<CollectionResponse<CreatedPublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedPublisherResp> response = this.webClient.get()
            .uri(uriBuilder.build(userId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedPublisherResp> getCreatedPublisher(Long publisherId, Long userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "publishers", "{publisherId}", "user", "{userId}");
    	ParameterizedTypeReference<CollectionResponse<CreatedPublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedPublisherResp> response = this.webClient.get()
            .uri(uriBuilder.build(publisherId, userId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<CreatedPublisherResp> createUserPublisher(CreatedPublisherReqPost request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "created", "publishers");
    	ParameterizedTypeReference<CollectionResponse<CreatedPublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<CreatedPublisherResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    
    
}

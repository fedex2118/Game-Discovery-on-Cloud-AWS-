package com.gateway.gateway_api.games.services;

import java.util.List;
import java.util.Set;

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
import com.gateway.gateway_api.games.data.classes.DeveloperReqPatch;
import com.gateway.gateway_api.games.data.classes.DeveloperReqPost;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.GameReqPatch;
import com.gateway.gateway_api.games.data.classes.GameReqPatchStatus;
import com.gateway.gateway_api.games.data.classes.GameReqPost;
import com.gateway.gateway_api.games.data.classes.GameReqPut;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GameRespTimestamp;
import com.gateway.gateway_api.games.data.classes.GameStatusResp;
import com.gateway.gateway_api.games.data.classes.GenreResp;
import com.gateway.gateway_api.games.data.classes.PlatformResp;
import com.gateway.gateway_api.games.data.classes.PublisherReqPatch;
import com.gateway.gateway_api.games.data.classes.PublisherReqPost;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPost;
import com.gateway.gateway_api.games.data.classes.UpdatableGameReqPut;
import com.gateway.gateway_api.games.data.classes.UpdatableGameResp;

@Service
public class GamesRequesterService {

	private static final Logger logger = LoggerFactory.getLogger(GamesRequesterService.class);

	private final WebClient webClient;
	private final String baseUrl;

	@Autowired
    public GamesRequesterService(WebClient.Builder webClientBuilder,
    		AppProperties appProperties) {
    	this.baseUrl = appProperties.getMicroservicesConfig().getGamesBaseUrl();
        this.webClient = webClientBuilder
            .build();
    }
	
	
	public CollectionResponse<DeveloperResp> findDeveloperById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "developer", "{id}");
    	ParameterizedTypeReference<CollectionResponse<DeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<DeveloperResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GenreResp> findGenreById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "genre", "{id}");
    	ParameterizedTypeReference<CollectionResponse<GenreResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GenreResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<PlatformResp> findPlatformById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "platform", "{id}");
    	ParameterizedTypeReference<CollectionResponse<PlatformResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PlatformResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<PublisherResp> findPublisherById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "publisher", "{id}");
    	ParameterizedTypeReference<CollectionResponse<PublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PublisherResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> findGameById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "game", "{id}");
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> findGames(MultiValueMap<String, String> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> findGamesDiscovery(MultiValueMap<String, String> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games", "by-discovery-criteria").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> findGamesByIds(Set<Long> gameIds) {
		List<String> ids = gameIds.stream()
                .map(el -> String.valueOf(el))
                .toList();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.addAll("ids", ids);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games", "by-ids").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> create(GameReqPost request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "game");
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> update(Long id, GameReqPut request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "game", "{id}");
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.put()
            .uri(uriBuilder.build(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> delete(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "game", "{id}");
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.delete()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<PublisherResp> createPublisher(PublisherReqPost request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "publisher");
    	ParameterizedTypeReference<CollectionResponse<PublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PublisherResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<DeveloperResp> createDeveloper(DeveloperReqPost request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "developer");
    	ParameterizedTypeReference<CollectionResponse<DeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<DeveloperResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<DeveloperResp> updateDeveloperById(Long id, DeveloperReqPatch request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "developer", "{id}");
    	ParameterizedTypeReference<CollectionResponse<DeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<DeveloperResp> response = this.webClient.patch()
            .uri(uriBuilder.build(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<DeveloperResp> findDeveloperByName(String name) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "developer", "by-name", "{name}");
    	ParameterizedTypeReference<CollectionResponse<DeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<DeveloperResp> response = this.webClient.get()
            .uri(uriBuilder.build(name))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<PublisherResp> updatePublisherById(Long id, PublisherReqPatch request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "publisher", "{id}");
    	ParameterizedTypeReference<CollectionResponse<PublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PublisherResp> response = this.webClient.patch()
            .uri(uriBuilder.build(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<PublisherResp> findPublisherByName(String name) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "publisher", "by-name", "{name}");
    	ParameterizedTypeReference<CollectionResponse<PublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PublisherResp> response = this.webClient.get()
            .uri(uriBuilder.build(name))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameResp> patchReviewQuantityAndAvgRating(Long id, GameReqPatch request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "game", "{id}");
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.patch()
            .uri(uriBuilder.build(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	
	public CollectionResponse<Long> findAllGameIds() {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games", "ids");
    	ParameterizedTypeReference<CollectionResponse<Long>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<Long> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameStatusResp> patchStatus(List<GameReqPatchStatus> request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games", "status");
    	ParameterizedTypeReference<CollectionResponse<GameStatusResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameStatusResp> response = this.webClient.patch()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	
	public CollectionResponse<GameRespTimestamp> findAllGamesOnPending(MultiValueMap<String, String> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games", "with-pending-status").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<GameRespTimestamp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameRespTimestamp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameStatusResp> findAllGamesOnRejected() {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games", "with-rejected-status");
    	ParameterizedTypeReference<CollectionResponse<GameStatusResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameStatusResp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	
	public CollectionResponse<GameResp> deleteAllGamesByIds(List<Long> gameIds) {
		List<String> ids = gameIds.stream()
                .map(el -> String.valueOf(el))
                .toList();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.addAll("gameIds", ids);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<GameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameResp> response = this.webClient.delete()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<Long> getPendingGamesIds(List<String> ids) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.addAll("gameIds", ids);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "games", "ids", "with-pending-status").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<Long>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<Long> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<GameStatusResp> findGameStatus(Long gameId) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "game", "{id}", "status");
    	ParameterizedTypeReference<CollectionResponse<GameStatusResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<GameStatusResp> response = this.webClient.get()
            .uri(uriBuilder.build(gameId))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<PublisherResp> deletePublisherById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "publisher", "{id}");
    	ParameterizedTypeReference<CollectionResponse<PublisherResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<PublisherResp> response = this.webClient.delete()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<DeveloperResp> deleteDeveloperById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "developer", "{id}");
    	ParameterizedTypeReference<CollectionResponse<DeveloperResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<DeveloperResp> response = this.webClient.delete()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<UpdatableGameResp> findUpdatableGameById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "updatable", "game", "{id}");
    	ParameterizedTypeReference<CollectionResponse<UpdatableGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<UpdatableGameResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<UpdatableGameResp> findAllUpdatableGames(MultiValueMap<String, String> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "updatable", "games").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<UpdatableGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<UpdatableGameResp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<UpdatableGameResp> createUpdatableGame(UpdatableGameReqPost request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "updatable", "game");
    	ParameterizedTypeReference<CollectionResponse<UpdatableGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<UpdatableGameResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<UpdatableGameResp> updateUpdatableGame(Long id, UpdatableGameReqPut request) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "updatable", "game", "{id}");
    	ParameterizedTypeReference<CollectionResponse<UpdatableGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<UpdatableGameResp> response = this.webClient.put()
            .uri(uriBuilder.build(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<UpdatableGameResp> deleteUpdatableGameById(Long id) {
		
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "updatable","game", "{id}");
    	ParameterizedTypeReference<CollectionResponse<UpdatableGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<UpdatableGameResp> response = this.webClient.delete()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	public CollectionResponse<UpdatableGameResp> deleteAllUpdatableGamesByIds(List<Long> gameIds) {
		List<String> ids = gameIds.stream()
                .map(el -> String.valueOf(el))
                .toList();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.addAll("ids", ids);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "updatable", "games", "by-ids").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<UpdatableGameResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<UpdatableGameResp> response = this.webClient.delete()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
	}
	
	
	

}

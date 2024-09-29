package com.gateway.gateway_api.users.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.gateway.gateway_api.application.AppProperties;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.response.ResponseHandler;
import com.gateway.gateway_api.users.data.classes.AuthReq;
import com.gateway.gateway_api.users.data.classes.AuthResp;
import com.gateway.gateway_api.users.data.classes.TokenResp;
import com.gateway.gateway_api.users.data.classes.UserReqPatch;
import com.gateway.gateway_api.users.data.classes.UserReqPost;
import com.gateway.gateway_api.users.data.classes.UserResp;

@Service
public class UsersRequesterService {
	
	private static final Logger logger = LoggerFactory.getLogger(UsersRequesterService.class);

    private final WebClient webClient;
    private final String baseUrl;

    @Autowired
    public UsersRequesterService(WebClient.Builder webClientBuilder,
    		AppProperties appProperties) {
    	this.baseUrl = appProperties.getMicroservicesConfig().getAuthBaseUrl();
        this.webClient = webClientBuilder
            .build();
    }
    
    public CollectionResponse<AuthResp> authenticate(AuthReq request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "auth", "authenticate");
    	ParameterizedTypeReference<CollectionResponse<AuthResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<AuthResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<AuthResp> signUp(UserReqPost request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "auth", "sign-up");
    	ParameterizedTypeReference<CollectionResponse<AuthResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<AuthResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<AuthResp> refreshToken() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "auth", "refresh-token");
    	ParameterizedTypeReference<CollectionResponse<AuthResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<AuthResp> response = this.webClient.post()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<UserResp> getId() {
    	
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", userDetails.getUsername());
    	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "user", "id").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<UserResp>> responseType = new ParameterizedTypeReference<>() {};

        CollectionResponse<UserResp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<TokenResp> isTokenValid() {
    	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "token", "valid");
    	ParameterizedTypeReference<CollectionResponse<TokenResp>> responseType = new ParameterizedTypeReference<>() {};
    	
        CollectionResponse<TokenResp> response = this.webClient.get()
            .uri(uriBuilder.build().toUri())
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<UserResp> findUserById(Long id) {
    	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "user", "{id}");
    	ParameterizedTypeReference<CollectionResponse<UserResp>> responseType = new ParameterizedTypeReference<>() {};
    	
        CollectionResponse<UserResp> response = this.webClient.get()
            .uri(uriBuilder.build(id))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<UserResp> updateUserById(Long id, UserReqPatch request) {
    	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "user", "{id}");
    	ParameterizedTypeReference<CollectionResponse<UserResp>> responseType = new ParameterizedTypeReference<>() {};
    	
        CollectionResponse<UserResp> response = this.webClient.patch()
            .uri(uriBuilder.build(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
    public CollectionResponse<UserResp> deleteUserByEmail(String email) {
    	MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    	params.add("email", email);
    	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        		.pathSegment("v1", "user").queryParams(params);
    	ParameterizedTypeReference<CollectionResponse<UserResp>> responseType = new ParameterizedTypeReference<>() {};
    	
        CollectionResponse<UserResp> response = this.webClient.delete()
            .uri(uriBuilder.build(email))
            .retrieve()
            .bodyToMono(responseType)
            .onErrorResume(ResponseHandler::handleErrorResponse)
            .block();

        return response;
    }
    
}

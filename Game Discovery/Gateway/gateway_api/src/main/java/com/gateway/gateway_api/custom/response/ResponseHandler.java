package com.gateway.gateway_api.custom.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gateway.gateway_api.custom.exception.ApiError;

import reactor.core.publisher.Mono;

public class ResponseHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static <T> Mono<CollectionResponse<T>> handleErrorResponse(Throwable ex) {
        CollectionResponse<T> response = new CollectionResponse<>();
        if (ex instanceof WebClientResponseException) {
            WebClientResponseException wcre = (WebClientResponseException) ex;
            String responseBody = wcre.getResponseBodyAsString();

            try {
                if (wcre.getStatusCode().is4xxClientError() || wcre.getStatusCode().is5xxServerError()) {
                    // deserialize the error response from the service
                    ApiError apiError = new ObjectMapper().registerModule(new JavaTimeModule())
                    		.readValue(responseBody, ApiError.class);
                    System.err.println("API Error: " + apiError.getMessage());
                    // return a response with the actual error from service
                    String debugMessage = apiError.getDebugMessage() != null ? (": " + apiError.getDebugMessage()) : "";
                    response.getMessages().add(new Message(apiError.getMessage() + debugMessage, "" + apiError.getStatus().value()));
                    return Mono.just(response);
                } else {
                	// if necessary handle other type of errors here
                }
            } catch (Exception e) {
            	logger.error("Error deserializing the error response: " + e.getMessage());
            	responseBody = responseBody.replaceAll("\"", "'");
            	response.getMessages().add(new Message("Internal server error: " + responseBody, "500"));
                return Mono.just(response);
            }
        }
        // for other exceptions not related to WebClient responses
        response.getMessages().add(new Message("An error not related to WebClient responses occured. Call for assistance."
        		+ " The service may be not reachable or it may be down.", "500"));
        return Mono.just(response);
    }
}

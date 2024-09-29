package com.gateway.gateway_api.config;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

	private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

	@Bean
	WebClient.Builder webClientBuilder() {
		return WebClient.builder().baseUrl("http://default-api-url.com") // Default base URL
				.filter(addAuthorizationHeader())
				.filter(logRequest())
				.defaultHeader("Content-Type", "application/json"); // Default headers
	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			// retrieve trace ID from MDC or generate a new one if not present
			String traceId = Optional.ofNullable(MDC.get("traceId")).orElse(UUID.randomUUID().toString());

			// log the request with the trace ID at info level. No sensitive information here
			logger.info("Trace ID: {} - Request: {} {}", traceId, clientRequest.method(), clientRequest.url());
			
			// ensure the trace ID is continued in the request
			// we do this by rebuilding the client request with the new header 
			// since ClientRequests are immutable
	        ClientRequest.Builder requestBuilder = ClientRequest.from(clientRequest);
	        requestBuilder.header("X-Trace-Id", traceId);
	        
	        clientRequest = requestBuilder.build();
	        
			clientRequest.headers().forEach((name, values) -> values
					.forEach(value -> logger.info("Header: {}={}", name, value)));

	        return Mono.just(clientRequest);
		});
	}
	
    private String retrieveToken() {
    	String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwt;
    }

    public ExchangeFilterFunction addAuthorizationHeader() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            String token = retrieveToken();
            // all the calls except the calls that don't require the token
            if(token != null && !token.isBlank()) {
            	return Mono.just(clientRequest)
                        .map(req -> ClientRequest.from(req)
                                .header("Authorization", "Bearer " + token)
                                .build());
            }
         // all the calls on auth path
            return Mono.just(clientRequest)
                .map(req -> ClientRequest.from(req)
                        .build());
        });
    }
}

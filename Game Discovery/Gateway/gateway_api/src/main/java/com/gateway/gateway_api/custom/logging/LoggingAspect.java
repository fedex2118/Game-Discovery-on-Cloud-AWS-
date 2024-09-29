package com.gateway.gateway_api.custom.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	private ObjectWriter objectWriter = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();

    @Before("execution(* com.gateway.gateway_api.users.services.UsersRequesterService*.*(..))"
    		+ "|| execution(* com.gateway.gateway_api.preferences.services.PreferencesRequesterService*.*(..))"
    		+ "|| execution(* com.gateway.gateway_api.games.services.GamesRequesterService*.*(..))"
    		+ "|| execution(* com.gateway.gateway_api.reviews.services.ReviewsRequesterService*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
    	MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();

        // method entry information
        logger.debug("Entering method: {}", methodSig.getMethod().getName());
        
        // retrieve the trace ID
        String traceId = MDC.get("traceId");
        
        try {
        	// arguments
        	String argsAsJson = objectWriter.writeValueAsString(joinPoint.getArgs());
			logger.debug("Trace ID: [{}] - Request Payload: \n[{}]", traceId, argsAsJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

    }

    @AfterReturning(pointcut = "execution(* com.gateway.gateway_api.users.services.UsersRequesterService.*(..))"
    		+ "|| execution(* com.gateway.gateway_api.preferences.services.PreferencesRequesterService*.*(..))"
    		+ "|| execution(* com.gateway.gateway_api.games.services.GamesRequesterService*.*(..))"
    		+ "|| execution(* com.gateway.gateway_api.reviews.services.ReviewsRequesterService*.*(..))", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
        
        // method exit information
        logger.debug("Exiting method: {}", methodSig.getMethod().getName());
        
        // retrieve the trace ID
        String traceId = MDC.get("traceId");
        
        try {
        	// result object
        	String resultAsJson = objectWriter.writeValueAsString(result);
			logger.debug("Trace ID: [{}] - Response Payload: \n[{}]", traceId, resultAsJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
}


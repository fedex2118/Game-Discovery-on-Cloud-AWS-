package com.users.users_api.custom.handler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.users.users_api.custom.exception.ApiError;
import com.users.users_api.custom.exception.UserReadableException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserReadableException.class)
    public ResponseEntity<Object> handleUserReadableException(UserReadableException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", ex.getCode());
        body.put("level", "ERROR");
        body.put("message", ex.getMessage());
        
        int httpStatus = retrieveStatus(ex.getCode());

        return new ResponseEntity<>(body, HttpStatus.valueOf(httpStatus));
    }
    
    private int retrieveStatus(String code) {
    	// default is 400
    	int result = 400;
    	
    	try {
    		result = Integer.parseInt(code);
    	} catch(NumberFormatException ex) {
    		return result;
    	}
    	
    	return result;
    }
    
    @ExceptionHandler({ ConstraintViolationException.class} )
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException e) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Validation error", e.getConstraintViolations().toString());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class} )
    public ResponseEntity<ApiError> handleConstraintSqlViolation(SQLIntegrityConstraintViolationException e) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "SQL Validation error", e.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ EntityNotFoundException.class} )
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Entity not found");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ ExpiredJwtException.class} )
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException e) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "JWT token expired");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ MalformedJwtException.class })
    public ResponseEntity<ApiError> handleMalformedJwt(MalformedJwtException e) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid JWT token");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ApiError> handleAccessDeniedException(
      Exception ex, WebRequest request) {
    	ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage());
    	
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ApiError> handleBadCredentialsException(
      Exception ex, WebRequest request) {
    	ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Bad Credentials", ex.getMessage());
    	
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @ExceptionHandler({ NoSuchElementException.class })
    public ResponseEntity<ApiError> handleNoSuchElementException(
      Exception ex, WebRequest request) {
    	ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "No resource found", ex.getMessage());
    	
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @ExceptionHandler({ JpaSystemException.class })
    public ResponseEntity<ApiError> handleJpaSystemException(
      Exception ex, WebRequest request) {
    	ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Integrity Violation", ex.getMessage());
    	
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @ExceptionHandler({ PropertyReferenceException.class })
    public ResponseEntity<ApiError> PropertyReferenceException(
      Exception ex, WebRequest request) {
    	ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Property reference error", ex.getMessage());
    	
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Error");
        errors.put("validationErrors", fieldErrors);
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

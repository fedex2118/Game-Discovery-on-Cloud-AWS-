package com.gateway.gateway_api.custom.handler;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.gateway_api.custom.exception.ApiError;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class FilterChainExceptionHandlingFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (RuntimeException e) {
			// to handle specific exceptions not catched by the exception handler
			handleException(response, e);
		}
	}

	private void handleException(HttpServletResponse response, RuntimeException e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
				e.getMessage());
		Integer statusCode = HttpStatus.BAD_REQUEST.value();

		// to handle other types of exceptions
		apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
		statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

		response.setStatus(statusCode);
		response.setContentType("application/json");

		try {
			String json = convertObjectToJson(apiError);
			response.getWriter().write(json);
		} catch (IOException ioException) {
			// Log this error or handle it as needed
			ioException.printStackTrace();
		}

	}

	private String convertObjectToJson(Object object) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
}

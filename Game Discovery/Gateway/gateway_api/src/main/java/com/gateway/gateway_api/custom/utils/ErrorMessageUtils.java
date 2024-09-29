package com.gateway.gateway_api.custom.utils;

import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.response.Message;

public class ErrorMessageUtils {

	public static <T> void sendErrorResponse(CollectionResponse<T> response) {
		Message errorMessage = response.getMessages().stream().findFirst().orElseThrow();
		throw new UserReadableException(errorMessage.getText(), errorMessage.getStatusCode());
	}
	
	public static <T> void sendErrorResponse(CollectionResponse<T> response,
			String customMessage) {
		Message errorMessage = response.getMessages().stream().findFirst().orElse(new Message(""));
		throw new UserReadableException(errorMessage.getText()
				+ " - " + customMessage, errorMessage.getStatusCode());
	}
}

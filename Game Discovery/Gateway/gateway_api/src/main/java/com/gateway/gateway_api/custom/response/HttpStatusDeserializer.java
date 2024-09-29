package com.gateway.gateway_api.custom.response;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class HttpStatusDeserializer extends JsonDeserializer<HttpStatus> {

	@Override
	public HttpStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {
		if (jsonParser.getCurrentToken().isNumeric()) {
			int statusCode = jsonParser.getIntValue();
			return HttpStatus.valueOf(statusCode);
		}
		String statusString = jsonParser.getText().trim();
		try {
			int statusCode = Integer.parseInt(statusString);
			return HttpStatus.valueOf(statusCode);
		} catch (NumberFormatException e) {
			return HttpStatus.valueOf(400);
		}

	}
}
package com.gateway.gateway_api.custom.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.users.data.classes.TokenResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	
	@Autowired
	UsersRequesterService usersRequesterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	SecurityContextHolder.getContext().getAuthentication().getCredentials();
    	
    	// send the request to the Authentication Service
    	CollectionResponse<TokenResp> tokenResponse = usersRequesterService.isTokenValid();
    	
    	if(tokenResponse.getContent().isEmpty() && !tokenResponse.getMessages().isEmpty()) {
    		ErrorMessageUtils.sendErrorResponse(tokenResponse);
    	}
    	
    	if(tokenResponse.getContent().isEmpty()) {
    		throw new UserReadableException("Bad Credentials", "401");
    	}
    	
        return true;
    }
}

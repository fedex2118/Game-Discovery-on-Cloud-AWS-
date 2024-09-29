package com.gateway.gateway_api.users.services;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.users.data.classes.AuthReq;
import com.gateway.gateway_api.users.data.classes.AuthResp;
import com.gateway.gateway_api.users.data.classes.UserReqPost;
import com.gateway.gateway_api.users.mapper.UsersMapper;
import com.gateway.gateway_api.users.request.dto.UserSignUpPost;

@Service
public class AuthService implements IAuthService {
	
	@Autowired
	private UsersRequesterService usersRequesterService;
	
	private UsersMapper usersMapper = Mappers.getMapper(UsersMapper.class);

	@Override
	public CollectionResponse<AuthResp> authenticate(AuthReq authReq) {
		
		CollectionResponse<AuthResp> response = usersRequesterService.authenticate(authReq);
		
		// handle errors
		if(response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}
	
	@Override
	public CollectionResponse<AuthResp> signUp(UserSignUpPost userSignUp) {
		
		// first call the user service to create the new user
		// map the corresponding properties
		UserReqPost userReq = usersMapper.toUserReqPost(userSignUp);
		
		// create user and generate token response
		CollectionResponse<AuthResp> response = usersRequesterService.signUp(userReq);
		
		// handle errors
		if(response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}
	
	@Override
	public CollectionResponse<AuthResp> refreshToken() {
		
		CollectionResponse<AuthResp> response = usersRequesterService.refreshToken();
		
		// handle errors
		if(response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		return response;
	}
	
	
}

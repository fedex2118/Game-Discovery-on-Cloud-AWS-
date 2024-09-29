package com.gateway.gateway_api.users.services;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.users.data.classes.AuthReq;
import com.gateway.gateway_api.users.data.classes.AuthResp;
import com.gateway.gateway_api.users.request.dto.UserSignUpPost;

public interface IAuthService {

	CollectionResponse<AuthResp> authenticate(AuthReq authReq);

	CollectionResponse<AuthResp> signUp(UserSignUpPost userReq);

	CollectionResponse<AuthResp> refreshToken();

}

package com.users.users_api.auth.service;

import java.io.IOException;

import com.users.users_api.auth.dto.AuthReq;
import com.users.users_api.auth.dto.AuthResp;
import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.users.dto.UserReqPost;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {

	CollectionResponse<AuthResp> signUp(UserReqPost userReq);

	CollectionResponse<AuthResp> authenticate(AuthReq authReq);

	CollectionResponse<AuthResp> refreshToken(HttpServletRequest request) throws IOException;

}

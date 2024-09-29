package com.gateway.gateway_api.users.mapper;

import org.mapstruct.Mapper;

import com.gateway.gateway_api.users.data.classes.UserReqPost;
import com.gateway.gateway_api.users.request.dto.UserSignUpPost;

@Mapper
public interface UsersMapper {

	UserReqPost toUserReqPost(UserSignUpPost userSignUp);
}

package com.gateway.gateway_api.users.services;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.users.data.classes.UserReqPatch;
import com.gateway.gateway_api.users.data.classes.UserResp;

public interface IUsersService {

	CollectionResponse<UserResp> getId();

	CollectionResponse<UserResp> findById(Long id);

	CollectionResponse<UserResp> updateById(Long id, UserReqPatch userReqPatch);

	CollectionResponse<UserResp> deleteUserByEmail(String email);

}

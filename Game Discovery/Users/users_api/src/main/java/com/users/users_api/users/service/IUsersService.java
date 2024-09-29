package com.users.users_api.users.service;

import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.users.dto.UserReqPatch;
import com.users.users_api.users.dto.UserReqPost;
import com.users.users_api.users.dto.UserResp;
import com.users.users_api.utils.Role;

import jakarta.servlet.http.HttpServletRequest;

public interface IUsersService {

	CollectionResponse<UserResp> create(UserReqPost userReq, Role role);

	CollectionResponse<UserResp> findById(Long id);

	CollectionResponse<UserResp> getId(String email);

	CollectionResponse<UserResp> findAll();

	CollectionResponse<UserResp> getEmail(Long id);

	/**
	 * This method deletes a user by its id. It doesn't deletes users that are admins for security reasons.
	 * It should only be used when user has the appropriate authorizations.
	 * @param id
	 * @return
	 */
	CollectionResponse<UserResp> deleteById(Long id);

	/**
	 * This method deletes the account passed if it corresponds to the email inside the token.
	 * It should be used when users delete their own account since is accessible to everyone.
	 * @param email
	 * @return
	 */
	CollectionResponse<UserResp> deleteByEmail(HttpServletRequest request, String email);

	CollectionResponse<UserResp> updateById(Long id, UserReqPatch userReqPatch);

}

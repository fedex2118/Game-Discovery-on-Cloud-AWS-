package com.users.users_api.roles.service;

import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.roles.dto.RoleResp;

public interface IRolesService {

	CollectionResponse<RoleResp> findById(Long id);

}

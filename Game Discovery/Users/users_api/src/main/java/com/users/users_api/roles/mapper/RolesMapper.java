package com.users.users_api.roles.mapper;

import org.mapstruct.Mapper;

import com.users.users_api.roles.dto.RoleResp;
import com.users.users_api.roles.entity.RoleEntity;

@Mapper
public interface RolesMapper {

	public RoleResp toRoleResponse(RoleEntity roleEntity);
}

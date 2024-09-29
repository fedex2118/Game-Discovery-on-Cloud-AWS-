package com.users.users_api.users.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.users.users_api.roles.entity.RoleEntity;
import com.users.users_api.users.dto.UserReqPost;
import com.users.users_api.users.dto.UserResp;
import com.users.users_api.users.entity.UserEntity;

@Mapper
public interface UsersMapper {

	UserEntity toUserEntity(UserReqPost userReqPost);
	
	@Mapping(target="roleName", source="role", qualifiedByName="toRoleName")
	UserResp toUserResponse(UserEntity userEntity);
	
	@Named("toRoleName")
	public default String toRoleName(RoleEntity roleEntity) {
		return roleEntity.getRoleName();
	}
	
	List<UserResp> toUsersResponse(List<UserEntity> userEntity);
	
}

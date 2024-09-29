package com.users.users_api.roles.service;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.roles.dto.RoleResp;
import com.users.users_api.roles.entity.RoleEntity;
import com.users.users_api.roles.mapper.RolesMapper;
import com.users.users_api.roles.repository.RolesRepository;
import com.users.users_api.utils.LoggerConstants;

@Service
public class RolesService implements IRolesService {

	private static final Logger logger = LoggerFactory.getLogger(RolesService.class);

	@Autowired
	private RolesRepository roleRepository;

	private RolesMapper rolesMapper = Mappers.getMapper(RolesMapper.class);

	@Override
	public CollectionResponse<RoleResp> findById(Long id) {
		RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
		
		RoleResp roleResp = new RoleResp();
		
		if(roleEntity != null) {
			logger.info(LoggerConstants.ROLE_ID_FOUND, roleEntity.getId());
			roleResp = rolesMapper.toRoleResponse(roleEntity);
		}

		return new CollectionResponse<>(roleResp);

	}
}

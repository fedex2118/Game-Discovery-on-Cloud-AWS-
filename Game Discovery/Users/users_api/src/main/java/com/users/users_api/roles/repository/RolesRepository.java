package com.users.users_api.roles.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.users_api.roles.entity.RoleEntity;

public interface RolesRepository extends JpaRepository<RoleEntity, Long>{

}

package com.users.users_api.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.users_api.users.entity.UserEntity;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {

	  Optional<UserEntity> findByEmail(String email);
}

package com.users.users_api.users.service;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.users.users_api.custom.exception.UserReadableException;
import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.roles.entity.RoleEntity;
import com.users.users_api.roles.repository.RolesRepository;
import com.users.users_api.security.JwtService;
import com.users.users_api.users.dto.UserReqPatch;
import com.users.users_api.users.dto.UserReqPost;
import com.users.users_api.users.dto.UserResp;
import com.users.users_api.users.entity.UserEntity;
import com.users.users_api.users.mapper.UsersMapper;
import com.users.users_api.users.repository.UsersRepository;
import com.users.users_api.utils.LoggerConstants;
import com.users.users_api.utils.Role;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UsersService implements IUsersService {
	
	private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

	private UsersMapper usersMapper = Mappers.getMapper(UsersMapper.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RolesRepository roleRepository;
	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	@Transactional
	public CollectionResponse<UserResp> create(UserReqPost userReq, Role role) {
		if (role == null) {
			throw new UserReadableException("Role is missing on request body!", "400");
		}
		
		UserEntity user = usersMapper.toUserEntity(userReq);

		// find the role
		RoleEntity roleEntity = roleRepository.findById(role.getId()).orElseThrow();
		
		logger.info(LoggerConstants.ROLE_ID_FOUND, roleEntity.getId());
		
		// set the user's role
		user.setRole(roleEntity);
		// encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// save the user
		user = userRepository.save(user);
		
		logger.info(LoggerConstants.USER_ID_FOUND, user.getId());
		
		UserResp userResp = usersMapper.toUserResponse(user);
		
		return new CollectionResponse<>(userResp);

	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "userIdCache", key = "#id" )
	public CollectionResponse<UserResp> findById(Long id) {
		UserEntity userEntity = userRepository.findById(id).orElse(null);
		
		if(userEntity != null) {
			logger.info(LoggerConstants.USER_ID_FOUND, userEntity.getId());
			UserResp userResp = usersMapper.toUserResponse(userEntity);
			return new CollectionResponse<>(userResp);
		}
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<UserResp> getId(String email) {
		UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
		
		if(userEntity != null) {
			logger.info(LoggerConstants.USER_ID_FOUND, userEntity.getId());
			UserResp userResp = new UserResp();
			userResp.setId(userEntity.getId());
			return new CollectionResponse<>(userResp);
		}
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<UserResp> findAll() {
		List<UserEntity> allUsers = userRepository.findAll();
		
		List<UserResp> allUsersResp = usersMapper.toUsersResponse(allUsers);
		
		return new CollectionResponse<>(allUsersResp);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<UserResp> getEmail(Long id) {
		UserEntity userEntity = userRepository.findById(id).orElse(null);
		
		if(userEntity != null) {
			logger.info(LoggerConstants.USER_ID_FOUND, userEntity.getId());
			UserResp userResp = new UserResp();
			userResp.setEmail(userEntity.getEmail());
			return new CollectionResponse<>(userResp);
		}
		
		return new CollectionResponse<>();
	}
	/**
	 * This method deletes a user by its id. It doesn't deletes users that are admins for security reasons.
	 * It should only be used when user has the appropriate authorizations.
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	//@CacheEvict(value = "userIdCache", key = "#id")
	public CollectionResponse<UserResp> deleteById(Long id) {
		UserEntity userEntity = userRepository.findById(id).orElseThrow();
		
		// for security reasons admin users should not be deleted from the API
		if(userEntity.getRole().getId() == Role.ADMIN.getId()) {
			throw new UserReadableException("Integrity Violation: cannot perform this action!", "403");
		}
		
		logger.info(LoggerConstants.USER_ID_DELETE, id);
		
		userRepository.deleteById(id);
		
		return new CollectionResponse<>();
	}
	
	/**
	 * This method deletes the account passed if it corresponds to the email inside the token.
	 * It should be used when users delete their own account since is accessible to everyone.
	 * @param email
	 * @return
	 */
	@Override
	@Transactional
	public CollectionResponse<UserResp> deleteByEmail(HttpServletRequest request, String email) {
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow();
		
		// for security reasons admin users should not be deleted from the API
		if(userEntity.getRole().getId() == Role.ADMIN.getId()) {
			throw new UserReadableException("Integrity Violation: cannot perform this action!", "403");
		}
		
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = authHeader.substring(7);
		String currentUserEmail = jwtService.extractUsername(token);
		
		// for security reasons delete current user only
		if(!userEntity.getEmail().equals(currentUserEmail)) {
			throw new UserReadableException("Integrity Violation: cannot perform this action!", "403");
		}
		
		logger.info(LoggerConstants.USER_ID_DELETE, userEntity.getId());
		
		// evict corresponding key on cache
		cacheManager.getCache("userIdCache").evict(userEntity.getId());
		
		userRepository.deleteById(userEntity.getId());
		
		return new CollectionResponse<>();
	}
	
	@Override
	public CollectionResponse<UserResp> updateById(Long id, UserReqPatch userReqPatch) {
		UserEntity userEntity = userRepository.findById(id).orElseThrow();
		
		logger.info(LoggerConstants.USER_ID_FOUND, id);
		
		// map properties
		userEntity.setUsername(userReqPatch.getUsername());
		
		// apply update
		userRepository.save(userEntity);
		
		logger.info(LoggerConstants.USER_ID_UPDATE, id, userReqPatch.getUsername());
		
		UserResp userResp = new UserResp();
		userResp.setUsername(userEntity.getUsername());
		
		return new CollectionResponse<>(userResp);
	}
}

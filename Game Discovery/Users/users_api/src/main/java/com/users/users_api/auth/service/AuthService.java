package com.users.users_api.auth.service;

import java.io.IOException;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.users.users_api.auth.dto.AuthReq;
import com.users.users_api.auth.dto.AuthResp;
import com.users.users_api.custom.exception.UserReadableException;
import com.users.users_api.custom.response.CollectionResponse;
import com.users.users_api.roles.entity.RoleEntity;
import com.users.users_api.roles.repository.RolesRepository;
import com.users.users_api.security.AuthenticationConfig;
import com.users.users_api.security.JwtService;
import com.users.users_api.users.dto.UserReqPost;
import com.users.users_api.users.entity.UserEntity;
import com.users.users_api.users.mapper.UsersMapper;
import com.users.users_api.users.repository.UsersRepository;
import com.users.users_api.utils.LoggerConstants;
import com.users.users_api.utils.Role;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService implements IAuthService {

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;

	private UsersMapper usersMapper = Mappers.getMapper(UsersMapper.class);
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Override
	public CollectionResponse<AuthResp> signUp(UserReqPost userReq) {
		UserEntity user = usersMapper.toUserEntity(userReq);
		RoleEntity roleEntity = null;
		
		// by default signing up with USER role
		// find the USER role, 2 must always be USER
		roleEntity = rolesRepository.findById(2L).orElseThrow();
		
		logger.info(LoggerConstants.ROLE_ID_FOUND, roleEntity.getId());
			
		if(!roleEntity.getRoleName().equals(Role.USER.getName())) {
			throw new UserReadableException("Integrity violation: 'USER' role is not defined.", "400");
		}
		// set the user's role
		user.setRole(roleEntity);
		
		// encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		logger.info("Processing user creation with username: {}", userReq.getUsername());
		
		// save the user
		UserEntity userEntity = usersRepository.save(user);
		
		logger.info("User created with ID: {}", userEntity.getId());
		
		// generate access token and refresh token
		UserDetails userDetails = AuthenticationConfig.toUserDetails(user);
		String accessToken = jwtService.generateToken(userDetails);
		String refreshToken = jwtService.generateRefreshToken(userDetails);

		return new CollectionResponse<AuthResp>(new AuthResp(accessToken, refreshToken));
	}

	@Override
	public CollectionResponse<AuthResp> authenticate(AuthReq authReq) {
		// authentication
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword()));
		// find user
		UserEntity user = usersRepository.findByEmail(authReq.getEmail()).orElseThrow();
		
		logger.info(LoggerConstants.USER_ID_FOUND, user.getId());
		
		// generate access token and refresh token
		UserDetails userDetails = AuthenticationConfig.toUserDetails(user);
		String accessToken = jwtService.generateToken(userDetails);
		String refreshToken = jwtService.generateRefreshToken(userDetails);

		return new CollectionResponse<AuthResp>(new AuthResp(accessToken, refreshToken));
	}

	@Override
	public CollectionResponse<AuthResp> refreshToken(HttpServletRequest request) 
			throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new UserReadableException("No JWT token found", "400");
		}
		// there must be a refresh token or an access token on the header to generate a new token
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			// try to find it via userDetailsService
			UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
			// if not found check if the user actually exists
			if(userDetails == null) {
				// find the user by email
				UserEntity user = this.usersRepository.findByEmail(userEmail).orElseThrow();
				// generate its details
				userDetails = AuthenticationConfig.toUserDetails(user);
			}
			
			// if refresh token is valid
			if (!jwtService.isAccessToken(refreshToken) && jwtService.isTokenValid(refreshToken, userDetails)) {
				// generate new access token
				String accessToken = jwtService.generateToken(userDetails);
				// the response will have the new generated access token and the old refresh token
				AuthResp authResponse = new AuthResp(accessToken, refreshToken);
				// build the authentication response
				return new CollectionResponse<AuthResp>(authResponse);
			}
			// token is not valid
			throw new UserReadableException("Invalid JWT token", "400");
		}
		return null;
	}
}

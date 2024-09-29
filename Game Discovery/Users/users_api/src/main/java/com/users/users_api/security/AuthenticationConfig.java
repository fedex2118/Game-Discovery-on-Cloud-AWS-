package com.users.users_api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.users.users_api.auditing.ApplicationAuditionAware;
import com.users.users_api.users.entity.UserEntity;
import com.users.users_api.users.repository.UsersRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthenticationConfig {
	
	@Autowired
	private UsersRepository userRepository;

    @Bean
    UserDetailsService userDetailsService() {
	    return email -> userRepository.findByEmail(email)
	            .map(userEntity -> toUserDetails(userEntity))
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}


	static public UserDetails toUserDetails(UserEntity userEntity) {
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    authorities.add(new SimpleGrantedAuthority(userEntity.getRole().getRoleName()));
	    
	    return new User(
	            userEntity.getEmail(),
	            userEntity.getPassword(),
	            authorities);
	}


	@Bean
	AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService());
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}

    @Bean
    AuditorAware<Long> auditorAware() {
		return new ApplicationAuditionAware();
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

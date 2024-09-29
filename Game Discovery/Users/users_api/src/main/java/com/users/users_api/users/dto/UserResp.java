package com.users.users_api.users.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonView;
import com.users.users_api.custom.view.UserRespView;

public class UserResp {
	
	@JsonView({UserRespView.All.class, UserRespView.GetId.class,
		UserRespView.Post.class})
	private Long id;
	
	@JsonView({UserRespView.All.class, UserRespView.Get.class, UserRespView.Post.class, 
		UserRespView.Patch.class})
	private String username;
	
	@JsonView({UserRespView.All.class, UserRespView.GetEmail.class,
		UserRespView.Post.class})
	private String email;
	
	@JsonView({UserRespView.All.class, UserRespView.Post.class, UserRespView.Get.class})
	private Instant createdAt;
	
	@JsonView({UserRespView.All.class, UserRespView.Post.class, UserRespView.Get.class})
	private Instant updatedAt;
	
	@JsonView({UserRespView.All.class, UserRespView.Post.class})
	private String roleName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}

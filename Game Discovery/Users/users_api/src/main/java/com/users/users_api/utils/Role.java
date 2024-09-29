package com.users.users_api.utils;

public enum Role {

	ADMIN(1L,"ADMIN"), USER(2L,"USER");
	
	private Long id;
	private String name;

	Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Long getId() {
		return this.id;
	}
	
}

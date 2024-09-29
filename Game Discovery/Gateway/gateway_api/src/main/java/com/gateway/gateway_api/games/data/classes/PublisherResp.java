package com.gateway.gateway_api.games.data.classes;

import java.util.Objects;

public class PublisherResp {

	private Long id;
	
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public PublisherResp() {

	}
	
	public PublisherResp(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PublisherResp other = (PublisherResp) obj;
		return Objects.equals(id, other.id);
	}
	
	
}

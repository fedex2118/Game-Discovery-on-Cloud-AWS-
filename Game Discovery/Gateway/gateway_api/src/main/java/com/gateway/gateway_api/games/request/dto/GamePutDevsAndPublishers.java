package com.gateway.gateway_api.games.request.dto;

import java.util.Set;

public class GamePutDevsAndPublishers {
	
	private Set<Long> publisherIds;
	private Set<Long> developerIds;

	public Set<Long> getPublisherIds() {
		return publisherIds;
	}

	public void setPublisherIds(Set<Long> publisherIds) {
		this.publisherIds = publisherIds;
	}

	public Set<Long> getDeveloperIds() {
		return developerIds;
	}

	public void setDeveloperIds(Set<Long> developerIds) {
		this.developerIds = developerIds;
	}
	
	
}

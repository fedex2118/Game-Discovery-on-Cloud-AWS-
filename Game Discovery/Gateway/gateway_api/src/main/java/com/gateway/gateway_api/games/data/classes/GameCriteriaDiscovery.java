package com.gateway.gateway_api.games.data.classes;

import java.util.Set;

public class GameCriteriaDiscovery extends GameCriteria {

	private Set<Long> excludedGameIds;

	public Set<Long> getExcludedGameIds() {
		return excludedGameIds;
	}

	public void setExcludedGameIds(Set<Long> excludedGameIds) {
		this.excludedGameIds = excludedGameIds;
	}
	
	
}

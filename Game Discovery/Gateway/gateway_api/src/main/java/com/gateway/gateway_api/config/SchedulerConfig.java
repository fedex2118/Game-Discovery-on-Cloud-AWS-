package com.gateway.gateway_api.config;

import java.time.LocalTime;

public class SchedulerConfig {

	private LocalTime startingHour;

	public LocalTime getStartingHour() {
		return startingHour;
	}

	public void setStartingHour(LocalTime startingHour) {
		this.startingHour = startingHour;
	}
}

package com.gateway.gateway_api.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.gateway.gateway_api.config.DevConfig;
import com.gateway.gateway_api.config.JwtConfig;
import com.gateway.gateway_api.config.MicroservicesConfig;
import com.gateway.gateway_api.config.SchedulerConfig;

@Configuration
@ConfigurationProperties(prefix="application")
public class AppProperties {

	private DevConfig devConfig = new DevConfig();
	private JwtConfig jwtConfig = new JwtConfig();
	private MicroservicesConfig microservicesConfig = new MicroservicesConfig();
	private SchedulerConfig schedulerConfig = new SchedulerConfig();

	public DevConfig getDevConfig() {
		return devConfig;
	}

	public void setDevConfig(DevConfig devConfig) {
		this.devConfig = devConfig;
	}

	public JwtConfig getJwtConfig() {
		return jwtConfig;
	}

	public void setJwtConfig(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	public MicroservicesConfig getMicroservicesConfig() {
		return microservicesConfig;
	}

	public void setMicroservicesConfig(MicroservicesConfig microservicesConfig) {
		this.microservicesConfig = microservicesConfig;
	}

	public SchedulerConfig getSchedulerConfig() {
		return schedulerConfig;
	}

	public void setSchedulerConfig(SchedulerConfig schedulerConfig) {
		this.schedulerConfig = schedulerConfig;
	}
	
}

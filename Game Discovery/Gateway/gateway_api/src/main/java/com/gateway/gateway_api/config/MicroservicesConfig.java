package com.gateway.gateway_api.config;

public class MicroservicesConfig {

	private String authBaseUrl;
	private String gamesBaseUrl;
	private String preferencesBaseUrl;
	private String reviewsBaseUrl;
	
	public String getAuthBaseUrl() {
		return authBaseUrl;
	}
	public void setAuthBaseUrl(String authBaseUrl) {
		this.authBaseUrl = authBaseUrl;
	}
	public String getGamesBaseUrl() {
		return gamesBaseUrl;
	}
	public void setGamesBaseUrl(String gamesBaseUrl) {
		this.gamesBaseUrl = gamesBaseUrl;
	}
	public String getPreferencesBaseUrl() {
		return preferencesBaseUrl;
	}
	public void setPreferencesBaseUrl(String preferencesBaseUrl) {
		this.preferencesBaseUrl = preferencesBaseUrl;
	}
	public String getReviewsBaseUrl() {
		return reviewsBaseUrl;
	}
	public void setReviewsBaseUrl(String reviewsBaseUrl) {
		this.reviewsBaseUrl = reviewsBaseUrl;
	}
	
}

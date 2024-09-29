package com.gateway.gateway_api.games.data.classes;

import java.time.Instant;
import java.util.Set;

public class GameReqPut {

	private String name;
	
	private Instant releaseDate;
	
	private Double price;
	
	private Boolean isSingleplayer;
	
	private Boolean isMultiplayer;
	
	private Boolean isPvp;
	
	private Boolean isPve;
	
	private String description;
	
	private Boolean isTwoDimensional;
	
	private Boolean isThreeDimensional;
	
	private String coverArtUrl;
	
	private Set<Long> platformIds;
	
	private Set<Long> genreIds;
	
	private Set<Long> publisherIds;
	
	private Set<Long> developerIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getReleaseDate() {
		return releaseDate;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setReleaseDate(Instant releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Boolean getIsSingleplayer() {
		return isSingleplayer;
	}

	public void setIsSingleplayer(Boolean isSingleplayer) {
		this.isSingleplayer = isSingleplayer;
	}

	public Boolean getIsMultiplayer() {
		return isMultiplayer;
	}

	public void setIsMultiplayer(Boolean isMultiplayer) {
		this.isMultiplayer = isMultiplayer;
	}

	public Boolean getIsPvp() {
		return isPvp;
	}

	public void setIsPvp(Boolean isPvp) {
		this.isPvp = isPvp;
	}

	public Boolean getIsPve() {
		return isPve;
	}

	public void setIsPve(Boolean isPve) {
		this.isPve = isPve;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsTwoDimensional() {
		return isTwoDimensional;
	}

	public void setIsTwoDimensional(Boolean isTwoDimensional) {
		this.isTwoDimensional = isTwoDimensional;
	}

	public Boolean getIsThreeDimensional() {
		return isThreeDimensional;
	}

	public void setIsThreeDimensional(Boolean isThreeDimensional) {
		this.isThreeDimensional = isThreeDimensional;
	}

	public String getCoverArtUrl() {
		return coverArtUrl;
	}

	public void setCoverArtUrl(String coverArtUrl) {
		this.coverArtUrl = coverArtUrl;
	}

	public Set<Long> getPlatformIds() {
		return platformIds;
	}

	public void setPlatformIds(Set<Long> platformIds) {
		this.platformIds = platformIds;
	}

	public Set<Long> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(Set<Long> genreIds) {
		this.genreIds = genreIds;
	}

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

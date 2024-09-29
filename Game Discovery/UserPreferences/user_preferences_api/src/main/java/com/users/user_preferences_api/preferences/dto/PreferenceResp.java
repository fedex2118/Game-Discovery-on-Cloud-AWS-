package com.users.user_preferences_api.preferences.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public class PreferenceResp {

	private Long id;
	
	private Instant startingDate;

	private Double startingPrice;

	private Boolean isSingleplayer;

	private Boolean isMultiplayer;

	private Boolean isPvp;
	
	private Boolean isPve;

	private Boolean isTwoDimensional;

	private Boolean isThreeDimensional;
	
	private BigDecimal startingAverageRating;

	private Integer startingReviewQuantity;

	private Boolean skipGenres;

	private Boolean skipPublishers;

	private Boolean skipDevelopers;

	private Boolean skipPlatforms;

	private Set<Long> platformIds;

	private Set<Long> genreIds;

	private Set<Long> publisherIds;

	private Set<Long> developerIds;

	private Set<Long> libraryIds;
	
	private Set<Long> wishlistIds;

	private Set<Long> createdGamesIds;
	
	private Set<Long> createdDeveloperIds;
	
	private Set<Long> createdPublisherIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Instant startingDate) {
		this.startingDate = startingDate;
	}

	public Double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
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

	public BigDecimal getStartingAverageRating() {
		return startingAverageRating;
	}

	public void setStartingAverageRating(BigDecimal startingAverageRating) {
		this.startingAverageRating = startingAverageRating;
	}

	public Integer getStartingReviewQuantity() {
		return startingReviewQuantity;
	}

	public void setStartingReviewQuantity(Integer startingReviewQuantity) {
		this.startingReviewQuantity = startingReviewQuantity;
	}

	public Boolean getSkipGenres() {
		return skipGenres;
	}

	public void setSkipGenres(Boolean skipGenres) {
		this.skipGenres = skipGenres;
	}

	public Boolean getSkipPublishers() {
		return skipPublishers;
	}

	public void setSkipPublishers(Boolean skipPublishers) {
		this.skipPublishers = skipPublishers;
	}

	public Boolean getSkipDevelopers() {
		return skipDevelopers;
	}

	public void setSkipDevelopers(Boolean skipDevelopers) {
		this.skipDevelopers = skipDevelopers;
	}

	public Boolean getSkipPlatforms() {
		return skipPlatforms;
	}

	public void setSkipPlatforms(Boolean skipPlatforms) {
		this.skipPlatforms = skipPlatforms;
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

	public Set<Long> getLibraryIds() {
		return libraryIds;
	}

	public void setLibraryIds(Set<Long> libraryIds) {
		this.libraryIds = libraryIds;
	}

	public Set<Long> getWishlistIds() {
		return wishlistIds;
	}

	public void setWishlistIds(Set<Long> wishlistIds) {
		this.wishlistIds = wishlistIds;
	}

	public Set<Long> getCreatedGamesIds() {
		return createdGamesIds;
	}

	public void setCreatedGamesIds(Set<Long> createdGamesIds) {
		this.createdGamesIds = createdGamesIds;
	}

	public Set<Long> getCreatedDeveloperIds() {
		return createdDeveloperIds;
	}

	public void setCreatedDeveloperIds(Set<Long> createdDeveloperIds) {
		this.createdDeveloperIds = createdDeveloperIds;
	}

	public Set<Long> getCreatedPublisherIds() {
		return createdPublisherIds;
	}

	public void setCreatedPublisherIds(Set<Long> createdPublisherIds) {
		this.createdPublisherIds = createdPublisherIds;
	}
	
}

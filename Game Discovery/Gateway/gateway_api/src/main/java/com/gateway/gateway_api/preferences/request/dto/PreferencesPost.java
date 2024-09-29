package com.gateway.gateway_api.preferences.request.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class PreferencesPost {

	private Instant startingDate = Instant.EPOCH;

	private Double startingPrice = 0.0;

	private Boolean isSingleplayer = false;

	private Boolean isMultiplayer = false;

	private Boolean isPvp = false;
	
	private Boolean isPve = false;

	private Boolean isTwoDimensional = false;

	private Boolean isThreeDimensional = false;

    @DecimalMin(value = "0.00", message = "Field value cannot be less than 0.00")
    @DecimalMax(value = "10.00", message = "Field value cannot be more than 10.00")
	private BigDecimal startingAverageRating = new BigDecimal(0.00);

    @Min(value = 0, message = "Value must be equal or greater than 0.")
	private Integer startingReviewQuantity = 0;

	private Boolean skipGenres = true;

	private Boolean skipPublishers = true;

	private Boolean skipDevelopers = true;

	private Boolean skipPlatforms = true;
	
	@Size(min = 1, message = "The array must have at least one element.")
	private Set<Long> platformIds;
	
	@Size(min = 1, message = "The array must have at least one element.")
	private Set<Long> genreIds;
	
	@Size(min = 1, message = "The array must have at least one element.")
	private Set<Long> publisherIds;
	
	@Size(min = 1, message = "The array must have at least one element.")
	private Set<Long> developerIds;

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
	
}

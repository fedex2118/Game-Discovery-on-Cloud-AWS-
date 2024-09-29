package com.games.games_api.games.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class GameCriteria {
	
	public enum GameCriteriaFilter {
		GREATER(0),
		MINOR(1),
		EQUAL(2),
		BETWEEN(3);
		
		private int id;
		
		private GameCriteriaFilter(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
	
	@Size(min = 1, message = "This field must have at least one character inserted.")
	private String name;
	
	private Instant minReleaseDate;
	private Instant maxReleaseDate;
	
	private Double startingPrice;

	private Boolean isSingleplayer;
	private Boolean isMultiplayer;
	private Boolean isPvp;
	private Boolean isPve;
	private Boolean isTwoDimensional;
	private Boolean isThreeDimensional;
	
    @DecimalMin(value = "0.00", message = "Field value cannot be less than 0.00")
    @DecimalMax(value = "10.00", message = "Field value cannot be more than 10.00")
	private BigDecimal minAverageRating;
    @DecimalMin(value = "0.00", message = "Field value cannot be less than 0.00")
    @DecimalMax(value = "10.00", message = "Field value cannot be more than 10.00")
	private BigDecimal maxAverageRating;
	
	@Min(value = 0, message = "Value must be equal or greater than 0.")
	private Integer minReviewQuantity;
	@Min(value = 0, message = "Value must be equal or greater than 0.")
	private Integer maxReviewQuantity;
	
	private Set<Long> genreIds;
    private Set<Long> platformIds;
    private Set<Long> developerIds;
    private Set<Long> publisherIds;
    
    private GameCriteriaFilter releaseDateFilter;
    private GameCriteriaFilter averageRatingFilter;
    private GameCriteriaFilter reviewQuantityFilter;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Instant getMinReleaseDate() {
		return minReleaseDate;
	}
	public void setMinReleaseDate(Instant minReleaseDate) {
		this.minReleaseDate = minReleaseDate;
	}
	public Instant getMaxReleaseDate() {
		return maxReleaseDate;
	}
	public void setMaxReleaseDate(Instant maxReleaseDate) {
		this.maxReleaseDate = maxReleaseDate;
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
	public BigDecimal getMinAverageRating() {
		return minAverageRating;
	}
	public void setMinAverageRating(BigDecimal minAverageRating) {
		this.minAverageRating = minAverageRating;
	}
	public BigDecimal getMaxAverageRating() {
		return maxAverageRating;
	}
	public void setMaxAverageRating(BigDecimal maxAverageRating) {
		this.maxAverageRating = maxAverageRating;
	}
	public Integer getMinReviewQuantity() {
		return minReviewQuantity;
	}
	public void setMinReviewQuantity(Integer minReviewQuantity) {
		this.minReviewQuantity = minReviewQuantity;
	}
	public Integer getMaxReviewQuantity() {
		return maxReviewQuantity;
	}
	public void setMaxReviewQuantity(Integer maxReviewQuantity) {
		this.maxReviewQuantity = maxReviewQuantity;
	}
	public Set<Long> getGenreIds() {
		return genreIds;
	}
	public void setGenreIds(Set<Long> genreIds) {
		this.genreIds = genreIds;
	}
	public Set<Long> getPlatformIds() {
		return platformIds;
	}
	public void setPlatformIds(Set<Long> platformIds) {
		this.platformIds = platformIds;
	}
	public Set<Long> getDeveloperIds() {
		return developerIds;
	}
	public void setDeveloperIds(Set<Long> developerIds) {
		this.developerIds = developerIds;
	}
	public Set<Long> getPublisherIds() {
		return publisherIds;
	}
	public void setPublisherIds(Set<Long> publisherIds) {
		this.publisherIds = publisherIds;
	}
	
	
	public GameCriteriaFilter getReleaseDateFilter() {
		return releaseDateFilter;
	}
	public void setReleaseDateFilter(GameCriteriaFilter releaseDateFilter) {
		this.releaseDateFilter = releaseDateFilter;
	}
	public GameCriteriaFilter getAverageRatingFilter() {
		return averageRatingFilter;
	}
	public void setAverageRatingFilter(GameCriteriaFilter averageRatingFilter) {
		this.averageRatingFilter = averageRatingFilter;
	}
	public GameCriteriaFilter getReviewQuantityFilter() {
		return reviewQuantityFilter;
	}
	public void setReviewQuantityFilter(GameCriteriaFilter reviewQuantityFilter) {
		this.reviewQuantityFilter = reviewQuantityFilter;
	}
	
	@AssertTrue(message = "The field 'releaseDateFilter' is mandatory whenever the field 'minReleaseDate' is passed.")
	boolean checkIsReleaseDateFilterValid() {
		if(this.minReleaseDate != null) {
			return this.releaseDateFilter != null;
		}
		
		return true;
	}
	
	@AssertTrue(message = "The field 'minReleaseDate' is mandatory whenever the field 'maxReleaseDate' is passed.")
	boolean checkIsMinReleaseDatePassed() {
		if(this.maxReleaseDate != null) {
			return this.minReleaseDate != null;
		}
		
		return true;
	}
	
	@AssertTrue(message = "The field 'averageRatingFilter' is mandatory whenever the field 'minAverageRating' is passed.")
	boolean checkIsAverageRatingFilterValid() {
		if(this.minAverageRating != null) {
			return this.averageRatingFilter != null;
		}
		
		return true;
	}
	
	@AssertTrue(message = "The field 'minAverageRating' is mandatory whenever the field 'maxAverageRating' is passed.")
	boolean checkIsMinAverageRatingPassed() {
		if(this.maxAverageRating != null) {
			return this.minAverageRating != null;
		}
		
		return true;
	}
	
	@AssertTrue(message = "The field 'reviewQuantityFilter' is mandatory whenever the field 'minReviewQuantity' is passed.")
	boolean checkIsReviewQuantityFilterValid() {
		if(this.minReviewQuantity != null) {
			return this.reviewQuantityFilter != null;
		}
		
		return true;
	}
	
	@AssertTrue(message = "The field 'minReviewQuantity' is mandatory whenever the field 'maxReviewQuantity' is passed.")
	boolean checkIsMinReviewQuantityPassed() {
		if(this.maxReviewQuantity != null) {
			return this.minReviewQuantity != null;
		}
		
		return true;
	}
	@Override
	public String toString() {
		return "GameCriteria [name=" + name + ", minReleaseDate=" + minReleaseDate + ", maxReleaseDate="
				+ maxReleaseDate + ", isSingleplayer=" + isSingleplayer + ", isMultiplayer=" + isMultiplayer
				+ ", isPvp=" + isPvp + ", isPve=" + isPve + ", isTwoDimensional=" + isTwoDimensional
				+ ", isThreeDimensional=" + isThreeDimensional + ", minAverageRating=" + minAverageRating
				+ ", maxAverageRating=" + maxAverageRating + ", minReviewQuantity=" + minReviewQuantity
				+ ", maxReviewQuantity=" + maxReviewQuantity + ", genreIds=" + genreIds + ", platformIds=" + platformIds
				+ ", developerIds=" + developerIds + ", publisherIds=" + publisherIds + ", releaseDateFilter="
				+ releaseDateFilter + ", averageRatingFilter=" + averageRatingFilter + ", reviewQuantityFilter="
				+ reviewQuantityFilter + "]";
	}
	
	
	
}

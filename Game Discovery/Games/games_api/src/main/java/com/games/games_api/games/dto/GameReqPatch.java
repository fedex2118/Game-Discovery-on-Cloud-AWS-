package com.games.games_api.games.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class GameReqPatch {
	
    @DecimalMin(value = "0.00", message = "Rating cannot be less than 0.00")
    @DecimalMax(value = "10.00", message = "Rating cannot be more than 10.00")
	private BigDecimal averageRating;
	
	private Integer reviewQuantity;

	public BigDecimal getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(BigDecimal averageRating) {
		this.averageRating = averageRating;
	}

	public Integer getReviewQuantity() {
		return reviewQuantity;
	}

	public void setReviewQuantity(Integer reviewQuantity) {
		this.reviewQuantity = reviewQuantity;
	}
	
}

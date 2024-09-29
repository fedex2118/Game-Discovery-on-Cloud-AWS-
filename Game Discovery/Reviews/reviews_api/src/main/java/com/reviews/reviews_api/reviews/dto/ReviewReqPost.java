package com.reviews.reviews_api.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewReqPost {

	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long userId;
	
	@Min(value = 1, message = "Value must be equal or greater than 1.")
	@NotNull(message="Field is mandatory")
	private Long gameId;
	
	@Min(value = 0, message = "Value must be equal or greater than 0.")
	@Max(value = 10, message = "Value must be equal or lower than 10.")
	private Integer vote = 5;
	
	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String description;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

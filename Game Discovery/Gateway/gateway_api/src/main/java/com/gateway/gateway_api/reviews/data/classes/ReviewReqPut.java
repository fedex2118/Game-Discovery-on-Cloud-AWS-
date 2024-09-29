package com.gateway.gateway_api.reviews.data.classes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewReqPut {
	
	@Min(value = 0, message = "Value must be equal or greater than 0.")
	@Max(value = 10, message = "Value must be equal or lower than 10.")
	@NotNull(message="Field is mandatory")
	private Integer vote;
	
	@NotNull(message="Field is mandatory")
	@NotBlank(message="Field must have a value")
	private String description;

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

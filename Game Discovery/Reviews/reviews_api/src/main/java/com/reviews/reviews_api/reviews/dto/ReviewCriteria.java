package com.reviews.reviews_api.reviews.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ReviewCriteria {

	public enum ReviewCriteriaFilter {
		GREATER(0),
		MINOR(1),
		EQUAL(2),
		BETWEEN(3);
		
		private int id;
		
		private ReviewCriteriaFilter(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
	
	@Min(value = 0, message = "Value must be equal or greater than 0.")
	@Max(value = 10, message = "Value must be equal or lower than 10.")
	private Integer minVote;
	
	@Min(value = 0, message = "Value must be equal or greater than 0.")
	@Max(value = 10, message = "Value must be equal or lower than 10.")
	private Integer maxVote;
	
	private ReviewCriteriaFilter voteFilter;

	public Integer getMinVote() {
		return minVote;
	}

	public void setMinVote(Integer minVote) {
		this.minVote = minVote;
	}

	public Integer getMaxVote() {
		return maxVote;
	}

	public void setMaxVote(Integer maxVote) {
		this.maxVote = maxVote;
	}

	public ReviewCriteriaFilter getVoteFilter() {
		return voteFilter;
	}

	public void setVoteFilter(ReviewCriteriaFilter voteFilter) {
		this.voteFilter = voteFilter;
	}
	
	@AssertTrue(message = "The field 'voteFilter' is mandatory whenever the field 'minVote' is passed.")
	boolean checkIsVoteFilterValid() {
		if(this.minVote != null) {
			return this.voteFilter != null;
		}
		
		return true;
	}
	
	@AssertTrue(message = "The field 'minVote' is mandatory whenever the field 'maxVote' is passed.")
	boolean checkIsMinVotePassed() {
		if(this.maxVote != null) {
			return this.minVote != null;
		}
		
		return true;
	}
	
}

package com.reviews.reviews_api.reviews.dto;

public class ReviewUpdateResp extends ReviewResp {

	private Integer oldVote;

	public Integer getOldVote() {
		return oldVote;
	}

	public void setOldVote(Integer oldVote) {
		this.oldVote = oldVote;
	}

}

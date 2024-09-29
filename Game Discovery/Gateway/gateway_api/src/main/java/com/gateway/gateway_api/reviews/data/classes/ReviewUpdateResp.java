package com.gateway.gateway_api.reviews.data.classes;

public class ReviewUpdateResp extends ReviewResp {

	private Integer oldVote;

	public Integer getOldVote() {
		return oldVote;
	}

	public void setOldVote(Integer oldVote) {
		this.oldVote = oldVote;
	}

}

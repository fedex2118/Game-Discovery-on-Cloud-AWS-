package com.gateway.gateway_api.reviews.data.classes;

public class ReviewResp {

	private Long userId;
	
	private Long gameId;
	
	private Integer vote;
	
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

	@Override
	public String toString() {
		return "REVIEW: [userId=" + userId + ", gameId=" + gameId + ", vote=" + vote + ", description=" + description
				+ "]";
	}
	
	

}

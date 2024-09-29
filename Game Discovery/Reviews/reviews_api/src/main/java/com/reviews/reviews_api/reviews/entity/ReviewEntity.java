package com.reviews.reviews_api.reviews.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @EmbeddedId
    private ReviewEntityKey id;
    
    @Column(name = "vote")
    private Integer vote;
    
    @Column(name = "description")
    private String description;

	public ReviewEntityKey getId() {
		return id;
	}

	public void setId(ReviewEntityKey id) {
		this.id = id;
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
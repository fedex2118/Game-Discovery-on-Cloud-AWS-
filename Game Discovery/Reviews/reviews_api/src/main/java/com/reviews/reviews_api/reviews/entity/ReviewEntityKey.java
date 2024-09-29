package com.reviews.reviews_api.reviews.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ReviewEntityKey {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "game_id")
    private Long gameId;

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

	@Override
	public int hashCode() {
		return Objects.hash(gameId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReviewEntityKey other = (ReviewEntityKey) obj;
		return Objects.equals(gameId, other.gameId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "ReviewEntityKey [userId=" + userId + ", gameId=" + gameId + "]";
	}
    
}

package com.users.user_preferences_api.genres.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GenreEntityKey {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "genre_id")
    private Long genreId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(genreId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenreEntityKey other = (GenreEntityKey) obj;
		return Objects.equals(genreId, other.genreId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "GenreEntityKey [userId=" + userId + ", genreId=" + genreId + "]";
	}
    
}

package com.games.games_api.games.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.games.games_api.developers.entity.DeveloperEntity;
import com.games.games_api.games.listeners.GameEntityTimestampListener;
import com.games.games_api.genres.entity.GenreEntity;
import com.games.games_api.platforms.entity.PlatformEntity;
import com.games.games_api.publishers.entity.PublisherEntity;
import com.games.games_api.updatable_games.entity.UpdatableGameEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
@EntityListeners(GameEntityTimestampListener.class)
public class GameEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "game_id")
	private Long id;
	
	@Column(name = "game_name")
	private String name;
	
	@Column(name = "release_date")
	private Instant releaseDate;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "is_singleplayer")
	private Boolean isSingleplayer;
	
	@Column(name = "is_multiplayer")
	private Boolean isMultiplayer;
	
	@Column(name = "is_pvp")
	private Boolean isPvp;
	
	@Column(name = "is_pve")
	private Boolean isPve;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_2d")
	private Boolean isTwoDimensional;
	
	@Column(name = "is_3d")
	private Boolean isThreeDimensional;
	
	@Column(name = "cover_art_url")
	private String coverArtUrl;
	
	@Column(name = "average_rating", precision=4, scale=2)
	private BigDecimal averageRating;
	
	@Column(name = "review_quantity")
	private Integer reviewQuantity;
	
	@Column(name = "status")
	private int status;
	
	@Column(name = "timestamp")
	private Instant timestamp;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	  name = "game_genres",
	  joinColumns = @JoinColumn(name = "game_id"),
	  inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private Set<GenreEntity> genres = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	  name = "game_platforms",
	  joinColumns = @JoinColumn(name = "game_id"),
	  inverseJoinColumns = @JoinColumn(name = "platform_id")
	)
	private Set<PlatformEntity> platforms = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	  name = "game_developers",
	  joinColumns = @JoinColumn(name = "game_id"),
	  inverseJoinColumns = @JoinColumn(name = "developer_id")
	)
	private Set<DeveloperEntity> developers = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	  name = "game_publishers",
	  joinColumns = @JoinColumn(name = "game_id"),
	  inverseJoinColumns = @JoinColumn(name = "publisher_id")
	)
	private Set<PublisherEntity> publishers = new HashSet<>();
	
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private UpdatableGameEntity updatableGame;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getReleaseDate() {
		return releaseDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setReleaseDate(Instant releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Boolean getIsSingleplayer() {
		return isSingleplayer;
	}

	public void setIsSingleplayer(Boolean isSingleplayer) {
		this.isSingleplayer = isSingleplayer;
	}

	public Boolean getIsMultiplayer() {
		return isMultiplayer;
	}

	public void setIsMultiplayer(Boolean isMultiplayer) {
		this.isMultiplayer = isMultiplayer;
	}

	public Boolean getIsPvp() {
		return isPvp;
	}

	public void setIsPvp(Boolean isPvp) {
		this.isPvp = isPvp;
	}

	public Boolean getIsPve() {
		return isPve;
	}

	public void setIsPve(Boolean isPve) {
		this.isPve = isPve;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsTwoDimensional() {
		return isTwoDimensional;
	}

	public void setIsTwoDimensional(Boolean isTwoDimensional) {
		this.isTwoDimensional = isTwoDimensional;
	}

	public Boolean getIsThreeDimensional() {
		return isThreeDimensional;
	}

	public void setIsThreeDimensional(Boolean isThreeDimensional) {
		this.isThreeDimensional = isThreeDimensional;
	}

	public String getCoverArtUrl() {
		return coverArtUrl;
	}

	public void setCoverArtUrl(String coverArtUrl) {
		this.coverArtUrl = coverArtUrl;
	}

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

	public Set<GenreEntity> getGenres() {
		return genres;
	}

	public void setGenres(Set<GenreEntity> genres) {
		this.genres = genres;
	}

	public Set<PlatformEntity> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Set<PlatformEntity> platforms) {
		this.platforms = platforms;
	}

	public Set<DeveloperEntity> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<DeveloperEntity> developers) {
		this.developers = developers;
	}

	public Set<PublisherEntity> getPublishers() {
		return publishers;
	}

	public void setPublishers(Set<PublisherEntity> publishers) {
		this.publishers = publishers;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public UpdatableGameEntity getUpdatableGame() {
		return updatableGame;
	}

	public void setUpdatableGame(UpdatableGameEntity updatableGame) {
		this.updatableGame = updatableGame;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameEntity other = (GameEntity) obj;
		return Objects.equals(id, other.id);
	}
}

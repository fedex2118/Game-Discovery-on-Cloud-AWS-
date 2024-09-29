package com.gateway.gateway_api.games.data.classes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class GameResp {

	private Long id;
	
	private String name;
	
	private Instant releaseDate;
	
	private Double price;
	
	private Boolean isSingleplayer;
	
	private Boolean isMultiplayer;
	
	private Boolean isPvp;
	
	private Boolean isPve;
	
	private String description;
	
	private Boolean isTwoDimensional;
	
	private Boolean isThreeDimensional;
	
	private String coverArtUrl;
	
	private BigDecimal averageRating;
	
	private Integer reviewQuantity;

	private Set<PlatformResp> platforms;

	private Set<GenreResp> genres;

	private Set<PublisherResp> publishers;

	private Set<DeveloperResp> developers;
	
	private GameStatus status;
	
	public GameResp() {
		super();
	}

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

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Set<PlatformResp> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Set<PlatformResp> platforms) {
		this.platforms = platforms;
	}

	public Set<GenreResp> getGenres() {
		return genres;
	}

	public void setGenres(Set<GenreResp> genres) {
		this.genres = genres;
	}

	public Set<PublisherResp> getPublishers() {
		return publishers;
	}

	public void setPublishers(Set<PublisherResp> publishers) {
		this.publishers = publishers;
	}

	public Set<DeveloperResp> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<DeveloperResp> developers) {
		this.developers = developers;
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
		GameResp other = (GameResp) obj;
		return Objects.equals(id, other.id);
	}
	
	
}

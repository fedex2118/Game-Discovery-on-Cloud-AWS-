package com.users.user_preferences_api.preferences.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import com.users.user_preferences_api.created_developers.entity.CreatedDeveloperEntity;
import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;
import com.users.user_preferences_api.created_publishers.entity.CreatedPublisherEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_preferences")
public class PreferenceEntity {

	@Id
	@Column(name = "user_id")
	private Long id;
	
	@Column(name = "starting_date")
	private Instant startingDate;
	
	@Column(name = "starting_price")
	private Double startingPrice;
	
	@Column(name = "is_singleplayer")
	private Boolean isSingleplayer;
	
	@Column(name = "is_multiplayer")
	private Boolean isMultiplayer;
	
	@Column(name = "is_pvp")
	private Boolean isPvp;
	
	@Column(name = "is_pve")
	private Boolean isPve;
	
	@Column(name = "is_2d")
	private Boolean isTwoDimensional;
	
	@Column(name = "is_3d")
	private Boolean isThreeDimensional;
	
	@Column(name = "starting_average_rating", precision=4, scale=2)
	private BigDecimal startingAverageRating;
	
	@Column(name = "starting_review_quantity")
	private Integer startingReviewQuantity;
	
	@Column(name = "skip_genres")
	private Boolean skipGenres;
	
	@Column(name = "skip_publishers")
	private Boolean skipPublishers;
	
	@Column(name = "skip_developers")
	private Boolean skipDevelopers;
	
	@Column(name = "skip_platforms")
	private Boolean skipPlatforms;
	
    public Instant getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Instant startingDate) {
		this.startingDate = startingDate;
	}

	public Double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
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

	public BigDecimal getStartingAverageRating() {
		return startingAverageRating;
	}

	public void setStartingAverageRating(BigDecimal startingAverageRating) {
		this.startingAverageRating = startingAverageRating;
	}

	public Integer getStartingReviewQuantity() {
		return startingReviewQuantity;
	}

	public void setStartingReviewQuantity(Integer startingReviewQuantity) {
		this.startingReviewQuantity = startingReviewQuantity;
	}

	public Boolean getSkipGenres() {
		return skipGenres;
	}

	public void setSkipGenres(Boolean skipGenres) {
		this.skipGenres = skipGenres;
	}

	public Boolean getSkipPublishers() {
		return skipPublishers;
	}

	public void setSkipPublishers(Boolean skipPublishers) {
		this.skipPublishers = skipPublishers;
	}

	public Boolean getSkipDevelopers() {
		return skipDevelopers;
	}

	public void setSkipDevelopers(Boolean skipDevelopers) {
		this.skipDevelopers = skipDevelopers;
	}

	public Boolean getSkipPlatforms() {
		return skipPlatforms;
	}

	public void setSkipPlatforms(Boolean skipPlatforms) {
		this.skipPlatforms = skipPlatforms;
	}

	@ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_pref_platforms",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "platform_id")
    private Set<Long> platformIds;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_pref_genres",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "genre_id")
    private Set<Long> genreIds;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_pref_developers",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "developer_id")
    private Set<Long> developerIds;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_pref_publishers",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "publisher_id")
    private Set<Long> publisherIds;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_wishlist",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "game_id")
    private Set<Long> wishlistIds;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "user_library",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "game_id")
    private Set<Long> libraryIds;
    
    @OneToMany(mappedBy = "userPreference", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreatedGameEntity> createdGames;
    
    @OneToMany(mappedBy = "userPreference", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreatedDeveloperEntity> createdDevelopers;
    
    @OneToMany(mappedBy = "userPreference", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CreatedPublisherEntity> createdPublishers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Long> getPlatformIds() {
		return platformIds;
	}

	public void setPlatformIds(Set<Long> platformIds) {
		this.platformIds = platformIds;
	}

	public Set<Long> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(Set<Long> genreIds) {
		this.genreIds = genreIds;
	}

	public Set<Long> getDeveloperIds() {
		return developerIds;
	}

	public void setDeveloperIds(Set<Long> developerIds) {
		this.developerIds = developerIds;
	}

	public Set<Long> getPublisherIds() {
		return publisherIds;
	}

	public void setPublisherIds(Set<Long> publisherIds) {
		this.publisherIds = publisherIds;
	}

	public Set<Long> getWishlistIds() {
		return wishlistIds;
	}

	public void setWishlistIds(Set<Long> wishlistIds) {
		this.wishlistIds = wishlistIds;
	}

	public Set<Long> getLibraryIds() {
		return libraryIds;
	}

	public void setLibraryIds(Set<Long> libraryIds) {
		this.libraryIds = libraryIds;
	}

	public Set<CreatedGameEntity> getCreatedGames() {
		return createdGames;
	}

	public void setCreatedGames(Set<CreatedGameEntity> createdGames) {
		this.createdGames = createdGames;
	}

	public Set<CreatedDeveloperEntity> getCreatedDevelopers() {
		return createdDevelopers;
	}

	public void setCreatedDevelopers(Set<CreatedDeveloperEntity> createdDevelopers) {
		this.createdDevelopers = createdDevelopers;
	}

	public Set<CreatedPublisherEntity> getCreatedPublishers() {
		return createdPublishers;
	}

	public void setCreatedPublishers(Set<CreatedPublisherEntity> createdPublishers) {
		this.createdPublishers = createdPublishers;
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
		PreferenceEntity other = (PreferenceEntity) obj;
		return Objects.equals(id, other.id);
	}
	
	
}

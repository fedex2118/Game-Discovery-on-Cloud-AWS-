package com.games.games_api.updatable_games.entity;

import java.time.Instant;

import com.games.games_api.games.entity.GameEntity;
import com.games.games_api.games.listeners.UpdatableGameEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "updatable_games")
@EntityListeners(UpdatableGameEntityListener.class)
public class UpdatableGameEntity {

	@Id
	@Column(name = "game_id", nullable = false)
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
	
	@Column(name = "timestamp")
	private Instant timestamp;
	
    @OneToOne
    @PrimaryKeyJoinColumn(name = "game_id", referencedColumnName = "game_id")
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private GameEntity game;

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

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public GameEntity getGame() {
		return game;
	}

	public void setGame(GameEntity game) {
		this.game = game;
	}

	
}

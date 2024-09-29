package com.games.games_api.games.specifications;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.games.games_api.games.entity.GameEntity;

import jakarta.persistence.criteria.JoinType;

public class GameSpecifications {
	
    public static <T> Specification<T> nameContains(String nameFragment) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + 
        nameFragment.toLowerCase() + "%");
    }
    
    public static Specification<GameEntity> isReleasedBefore(Instant dateTime) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("releaseDate"), dateTime);
    }

    public static Specification<GameEntity> isReleasedAfter(Instant dateTime) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("releaseDate"), dateTime);
    }
    
    public static Specification<GameEntity> isReleasedEqual(Instant dateTime) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("releaseDate"), dateTime);
    }

    public static Specification<GameEntity> isReleasedBetween(Instant startDateTime, 
    		Instant endDateTime) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get("releaseDate"), startDateTime, endDateTime);
    }
    
    public static Specification<GameEntity> isPriceLessThan(Double startingPrice) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("price"), startingPrice);
    }

    public static Specification<GameEntity> isSingleplayer(boolean isSingleplayer) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isSingleplayer"), 
        		isSingleplayer);
    }

    public static Specification<GameEntity> isMultiplayer(boolean isMultiplayer) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isMultiplayer"), 
        		isMultiplayer);
    }
    
    public static Specification<GameEntity> isPvp(boolean isPvp) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isPvp"), isPvp);
    }
    
    public static Specification<GameEntity> isPve(boolean isPve) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isPve"), isPve);
    }
    
    public static Specification<GameEntity> isTwoDimensional(boolean isTwoDimensional) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isTwoDimensional"), 
        		isTwoDimensional);
    }
    
    public static Specification<GameEntity> isThreeDimensional(boolean isThreeDimensional) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isThreeDimensional"), isThreeDimensional);
    }
    
    public static Specification<GameEntity> hasAvgRatingLessThan(BigDecimal averageRatingThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("averageRating"), averageRatingThreshold);
    }

    public static Specification<GameEntity> hasAvgRatingGreaterThan(BigDecimal averageRatingThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("averageRating"), averageRatingThreshold);
    }

    public static Specification<GameEntity> hasAvgRatingEqualTo(BigDecimal averageRatingThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("averageRating"), averageRatingThreshold);
    }
    
    public static Specification<GameEntity> avgRatingIsBetween(BigDecimal minAvgRatingThreshold, 
    		BigDecimal maxAvgRatingThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get("averageRating"), minAvgRatingThreshold, maxAvgRatingThreshold);
    }
    
    public static Specification<GameEntity> hasReviewQuantityLessThan(int reviewQuantityThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("reviewQuantity"), reviewQuantityThreshold);
    }

    public static Specification<GameEntity> hasReviewQuantityGreaterThan(int reviewQuantityThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("reviewQuantity"), reviewQuantityThreshold);
    }

    public static Specification<GameEntity> hasReviewQuantityEqualTo(int reviewQuantityThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("reviewQuantity"), reviewQuantityThreshold);
    }
    
    public static Specification<GameEntity> reviewQuantityIsBetween(int minReviewQuantityThreshold, 
    		int maxReviewQuantityThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get("reviewQuantity"), minReviewQuantityThreshold, 
            		maxReviewQuantityThreshold);
    }
    
    public static Specification<GameEntity> withStatus(int status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), 
        		status);
    }
    
    public static Specification<GameEntity> hasGenres(Set<Long> genreIds) {
        return (root, query, cb) -> {
            if (genreIds == null || genreIds.isEmpty()) {
                return null;
            }
            root.join("genres", JoinType.INNER); // inner join
            query.distinct(true); // distinct
            return root.get("genres").get("id").in(genreIds);
        };
    }
    
    public static Specification<GameEntity> hasPlatforms(Set<Long> platformIds) {
        return (root, query, cb) -> {
            if (platformIds == null || platformIds.isEmpty()) {
                return null;
            }
            root.join("platforms", JoinType.INNER); // inner join
            query.distinct(true); // distinct
            return root.get("platforms").get("id").in(platformIds);
        };
    }
    
    public static Specification<GameEntity> hasDevelopers(Set<Long> developerIds) {
        return (root, query, cb) -> {
            if (developerIds == null || developerIds.isEmpty()) {
                return null;
            }
            root.join("developers", JoinType.INNER); // inner join
            query.distinct(true); // distinct
            return root.get("developers").get("id").in(developerIds);
        };
    }
    
    public static Specification<GameEntity> hasPublishers(Set<Long> publisherIds) {
        return (root, query, cb) -> {
            if (publisherIds == null || publisherIds.isEmpty()) {
                return null;
            }
            root.join("publishers", JoinType.INNER); // inner join
            query.distinct(true); // distinct
            return root.get("publishers").get("id").in(publisherIds);
        };
    }
    
    public static Specification<GameEntity> excludeGameIds(Set<Long> gameIdsToExclude) {
    	return (root, query, cb) -> {
            if (gameIdsToExclude == null || gameIdsToExclude.isEmpty()) {
                return null;
            }
            return cb.not(root.get("id").in(gameIdsToExclude));
        };
        
    }
}

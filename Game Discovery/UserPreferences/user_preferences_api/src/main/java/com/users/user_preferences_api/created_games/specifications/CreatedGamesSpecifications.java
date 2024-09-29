package com.users.user_preferences_api.created_games.specifications;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;

public class CreatedGamesSpecifications {

    public static Specification<CreatedGameEntity> isUpdatedAt(Instant updatedAt) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("updatedAt"), updatedAt);
    }
    
    public static Specification<CreatedGameEntity> byUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("userPreference").get("id"), userId);
    }
}

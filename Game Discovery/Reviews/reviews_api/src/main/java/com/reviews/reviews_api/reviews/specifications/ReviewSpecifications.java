package com.reviews.reviews_api.reviews.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.reviews.reviews_api.reviews.entity.ReviewEntity;

@Component
public class ReviewSpecifications {
    public static Specification<ReviewEntity> hasVoteLessThan(int voteThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("vote"), voteThreshold);
    }

    public static Specification<ReviewEntity> hasVoteGreaterThan(int voteThreshold) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("vote"), voteThreshold);
    }

    public static Specification<ReviewEntity> hasVoteEqualTo(int voteValue) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("vote"), voteValue);
    }
    
    public static Specification<ReviewEntity> voteIsBetween(int minVote, int maxVote) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get("vote"), minVote, maxVote);
    }
}

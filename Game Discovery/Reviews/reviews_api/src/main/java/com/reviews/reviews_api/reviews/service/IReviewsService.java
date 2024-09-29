package com.reviews.reviews_api.reviews.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.reviews.reviews_api.custom.response.CollectionResponse;
import com.reviews.reviews_api.reviews.dto.ReviewCriteria;
import com.reviews.reviews_api.reviews.dto.ReviewReqPost;
import com.reviews.reviews_api.reviews.dto.ReviewReqPut;
import com.reviews.reviews_api.reviews.dto.ReviewResp;
import com.reviews.reviews_api.reviews.dto.ReviewRespAvgRating;
import com.reviews.reviews_api.reviews.dto.ReviewRespCount;
import com.reviews.reviews_api.reviews.dto.ReviewUpdateResp;

public interface IReviewsService {

	CollectionResponse<ReviewResp> create(ReviewReqPost reviewReqPost);
	
	CollectionResponse<ReviewUpdateResp> update(Long gameId, Long userId, ReviewReqPut reviewReqPut);
	
	CollectionResponse<ReviewRespCount> retrieveCount(Long gameId);
	
	CollectionResponse<ReviewRespAvgRating> retrieveAvgRating(Long gameId);
	
	CollectionResponse<ReviewResp> findByKey(Long gameId, Long userId);

	CollectionResponse<ReviewResp> findAllByUserId(Long userId, Pageable pageable);

	CollectionResponse<ReviewResp> findByCriteria(ReviewCriteria reviewCriteria, Pageable pageable);

	CollectionResponse<ReviewResp> deleteUserReviews(Long userId);

	CollectionResponse<ReviewResp> deleteByKey(Long userId, Long gameId);

	CollectionResponse<ReviewResp> deleteGameReviews(Long gameId);

	CollectionResponse<ReviewResp> deleteAllReviewsByGameIds(List<Long> gameIds);

}

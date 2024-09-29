package com.gateway.gateway_api.reviews.services;

import org.springframework.data.domain.Pageable;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.reviews.data.classes.ReviewCriteria;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPost;
import com.gateway.gateway_api.reviews.data.classes.ReviewReqPut;
import com.gateway.gateway_api.reviews.data.classes.ReviewResp;
import com.gateway.gateway_api.reviews.data.classes.ReviewUpdateResp;

public interface IReviewsService {

	CollectionResponse<ReviewResp> create(ReviewReqPost reviewReqPost);
	
	CollectionResponse<ReviewUpdateResp> update(Long gameId, Long userId, ReviewReqPut request);

	CollectionResponse<ReviewResp> findByKey(Long gameId, Long userId);

	CollectionResponse<ReviewResp> findAllByUserId(Long userId, Pageable pageable);

	CollectionResponse<ReviewResp> findByCriteria(ReviewCriteria criteria, Pageable pageable);

	CollectionResponse<ReviewResp> deleteByKey(Long gameId, Long userId);



}

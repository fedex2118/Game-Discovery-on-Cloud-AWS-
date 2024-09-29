package com.reviews.reviews_api.reviews.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reviews.reviews_api.reviews.dto.ReviewReqPost;
import com.reviews.reviews_api.reviews.dto.ReviewReqPut;
import com.reviews.reviews_api.reviews.dto.ReviewResp;
import com.reviews.reviews_api.reviews.dto.ReviewUpdateResp;
import com.reviews.reviews_api.reviews.entity.ReviewEntity;

@Mapper
public interface ReviewsMapper {
	
	@Mapping(target = "userId", source = "id.userId")
	@Mapping(target = "gameId", source = "id.gameId")
	ReviewResp toReviewResponse(ReviewEntity reviewEntity);

	List<ReviewResp> toReviewsResponse(List<ReviewEntity> reviewEntities);
	
	ReviewUpdateResp toReviewResponseUpdate(ReviewResp response);
	
	@Mapping(target = "id.userId", source = "userId")
	@Mapping(target = "id.gameId", source = "gameId")
	ReviewEntity toReviewEntity(ReviewReqPost reviewReqPost);
	
	ReviewEntity toReviewEntity(ReviewReqPut reviewReqPut);
}

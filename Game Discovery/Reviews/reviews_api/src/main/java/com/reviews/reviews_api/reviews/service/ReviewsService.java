package com.reviews.reviews_api.reviews.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reviews.reviews_api.custom.exception.UserReadableException;
import com.reviews.reviews_api.custom.response.CollectionResponse;
import com.reviews.reviews_api.custom.utils.LoggerConstants;
import com.reviews.reviews_api.reviews.dto.ReviewCriteria;
import com.reviews.reviews_api.reviews.dto.ReviewReqPost;
import com.reviews.reviews_api.reviews.dto.ReviewReqPut;
import com.reviews.reviews_api.reviews.dto.ReviewResp;
import com.reviews.reviews_api.reviews.dto.ReviewRespAvgRating;
import com.reviews.reviews_api.reviews.dto.ReviewRespCount;
import com.reviews.reviews_api.reviews.dto.ReviewUpdateResp;
import com.reviews.reviews_api.reviews.entity.ReviewEntity;
import com.reviews.reviews_api.reviews.entity.ReviewEntityKey;
import com.reviews.reviews_api.reviews.mapper.ReviewsMapper;
import com.reviews.reviews_api.reviews.repository.ReviewsRepository;
import com.reviews.reviews_api.reviews.specifications.ReviewSpecifications;

@Service
public class ReviewsService implements IReviewsService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewsService.class);

	@Autowired
	private ReviewsRepository reviewsRepository;

	private ReviewsMapper reviewsMapper = Mappers.getMapper(ReviewsMapper.class);

//	@Autowired
//	private CacheManager cacheManager;

	@Override
	public CollectionResponse<ReviewResp> create(ReviewReqPost reviewReqPost) {
		
		// check that review doesn't exist already
		ReviewEntityKey key = new ReviewEntityKey();
		key.setGameId(reviewReqPost.getGameId());
		key.setUserId(reviewReqPost.getUserId());
		
		ReviewEntity reviewFound = reviewsRepository.findById(key).orElse(null);
		
		if(reviewFound != null) {
			throw new UserReadableException("Review already exist.", "400");
		}

		// save the entity
		ReviewEntity reviewEntity = reviewsMapper.toReviewEntity(reviewReqPost);

		reviewEntity = reviewsRepository.save(reviewEntity);

		ReviewResp response = reviewsMapper.toReviewResponse(reviewEntity);

		return new CollectionResponse<>(response);
	}

	@Override
	@Transactional
	public CollectionResponse<ReviewUpdateResp> update(Long gameId, Long userId, ReviewReqPut reviewReqPut) {

		// check that id exists
		ReviewEntityKey key = new ReviewEntityKey();
		key.setGameId(gameId);
		key.setUserId(userId);

		ReviewEntity oldReview = reviewsRepository.findById(key).orElseThrow();
		Integer oldVote = oldReview.getVote();

		// save the entity
		ReviewEntity reviewEntity = reviewsMapper.toReviewEntity(reviewReqPut);
		// map the id for update
		reviewEntity.setId(key);

		reviewEntity = reviewsRepository.save(reviewEntity);

		ReviewResp response = reviewsMapper.toReviewResponse(reviewEntity);
		
		ReviewUpdateResp respUpdate = reviewsMapper.toReviewResponseUpdate(response);
		
		// add old vote
		respUpdate.setOldVote(oldVote);

		return new CollectionResponse<>(respUpdate);
	}

	@Override
	@Transactional
	public CollectionResponse<ReviewRespCount> retrieveCount(Long gameId) {

		long count = reviewsRepository.countByGameId(gameId);

		ReviewRespCount response = new ReviewRespCount();
		response.setCount((int) count);

		return new CollectionResponse<>(response);
	}

	@Override
	@Transactional
	public CollectionResponse<ReviewRespAvgRating> retrieveAvgRating(Long gameId) {

		Double avgVote = reviewsRepository.findAverageRatingByGameId(gameId);
		// convert to BigDecimal and scale
		BigDecimal avgVoteBigDecimal = (avgVote != null) ? BigDecimal.valueOf(avgVote).setScale(2, RoundingMode.HALF_UP)
				: BigDecimal.ZERO;

		ReviewRespAvgRating response = new ReviewRespAvgRating();
		response.setAverageRating(avgVoteBigDecimal);

		return new CollectionResponse<>(response);
	}

	@Override
	@Transactional(readOnly = true)
	// @Cacheable(value = "reviewsCache", key = "#gameId + '_' + #userId" )
	public CollectionResponse<ReviewResp> findByKey(Long gameId, Long userId) {
		ReviewEntityKey key = new ReviewEntityKey();
		key.setGameId(gameId);
		key.setUserId(userId);

		ReviewEntity reviewFound = reviewsRepository.findById(key).orElse(null);

		if (reviewFound != null) {
			logger.info(LoggerConstants.REVIEW_GAME_ID_FOUND, reviewFound.getId());
			ReviewResp response = reviewsMapper.toReviewResponse(reviewFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}

	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<ReviewResp> findAllByUserId(Long userId, Pageable pageable) {
		Page<ReviewEntity> gamesFound = reviewsRepository.findAllByIdUserId(userId, pageable);

		if (gamesFound != null) {
			// logger.info(LoggerConstants._ID_FOUND, Found.getId());
			List<ReviewResp> response = reviewsMapper.toReviewsResponse(gamesFound.getContent());

			// convert it for pagination
			Page<ReviewResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());
			return new CollectionResponse<>(responsePage);
		}

		return new CollectionResponse<>();
	}

	@Override
	public CollectionResponse<ReviewResp> findByCriteria(ReviewCriteria reviewCriteria, Pageable pageable) {
		Specification<ReviewEntity> spec = Specification.where(null);

		// VOTE
		Integer minVote = reviewCriteria.getMinVote();

		if (minVote != null) {

			Integer maxVote = reviewCriteria.getMaxVote() != null ? reviewCriteria.getMaxVote() : 10;

			switch (reviewCriteria.getVoteFilter()) {
			case GREATER -> spec = spec.and(ReviewSpecifications.hasVoteGreaterThan(minVote));
			case MINOR -> spec = spec.and(ReviewSpecifications.hasVoteLessThan(minVote));
			case EQUAL -> spec = spec.and(ReviewSpecifications.hasVoteEqualTo(minVote));
			case BETWEEN -> spec = spec.and(ReviewSpecifications.voteIsBetween(minVote, maxVote));
			}
		}

		Page<ReviewEntity> reviewsFound = reviewsRepository.findAll(spec, pageable);

		if (reviewsFound != null) {
			List<ReviewResp> response = reviewsMapper.toReviewsResponse(reviewsFound.getContent());

			// convert it for pagination
			Page<ReviewResp> responsePage = new PageImpl<>(response, pageable, reviewsFound.getTotalElements());
			return new CollectionResponse<>(responsePage);
		}

		return new CollectionResponse<>();

	}

	@Override
	@Transactional
	// @CacheEvict(value = "reviewsCache", key = "#gameId + '_' + #userId")
	public CollectionResponse<ReviewResp> deleteByKey(Long gameId, Long userId) {
		ReviewEntityKey key = new ReviewEntityKey();
		key.setGameId(gameId);
		key.setUserId(userId);

		reviewsRepository.deleteById(key);

		return new CollectionResponse<>();
	}

	@Override
	@Transactional
	public CollectionResponse<ReviewResp> deleteUserReviews(Long userId) {
		// first find all games for the input user
		List<ReviewEntity> gamesFound = reviewsRepository.findAllByIdUserId(userId);

		if (!gamesFound.isEmpty()) {
			// delete all found game entries
			reviewsRepository.deleteAll(gamesFound);
		}

		return new CollectionResponse<>();
	}

	@Override
	@Transactional
	public CollectionResponse<ReviewResp> deleteGameReviews(Long gameId) {
		// first find all games with input ID
		List<ReviewEntity> gamesFound = reviewsRepository.findAllByIdGameId(gameId);

		// delete all found game entries
		if (!gamesFound.isEmpty()) {
			reviewsRepository.deleteAll(gamesFound);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<ReviewResp> deleteAllReviewsByGameIds(List<Long> gameIds) {
		
		reviewsRepository.deleteByGameIdIn(gameIds);
		

		return new CollectionResponse<>();
	}

}

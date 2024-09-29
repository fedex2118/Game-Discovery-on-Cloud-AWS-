package com.reviews.reviews_api.reviews.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reviews.reviews_api.reviews.entity.ReviewEntity;
import com.reviews.reviews_api.reviews.entity.ReviewEntityKey;

import jakarta.transaction.Transactional;

public interface ReviewsRepository extends JpaRepository<ReviewEntity, ReviewEntityKey>, JpaSpecificationExecutor<ReviewEntity>{

	List<ReviewEntity> findAllByIdGameId(Long gameId);
	List<ReviewEntity> findAllByIdUserId(Long userId);
	Page<ReviewEntity> findAllByIdUserId(Long userId, Pageable pageable);
	
    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.id.gameId = :gameId")
    long countByGameId(@Param("gameId") Long gameId);

    @Query("SELECT AVG(r.vote) FROM ReviewEntity r WHERE r.id.gameId = :gameId")
    Double findAverageRatingByGameId(@Param("gameId") Long gameId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ReviewEntity r WHERE r.id.gameId IN :gameIds")
    void deleteByGameIdIn(@Param("gameIds") List<Long> gameIds);
}

package com.games.games_api.games.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.util.CollectionUtils;

import com.games.games_api.custom.exception.UserReadableException;
import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.developers.service.IDevelopersService;
import com.games.games_api.games.dto.GameCriteria;
import com.games.games_api.games.dto.GameCriteriaDiscovery;
import com.games.games_api.games.dto.GameReqPatch;
import com.games.games_api.games.dto.GameReqPatchStatus;
import com.games.games_api.games.dto.GameReqPost;
import com.games.games_api.games.dto.GameReqPut;
import com.games.games_api.games.dto.GameResp;
import com.games.games_api.games.dto.GameRespTimestamp;
import com.games.games_api.games.dto.GameStatus;
import com.games.games_api.games.dto.GameStatusResp;
import com.games.games_api.games.entity.GameEntity;
import com.games.games_api.games.mapper.GamesMapper;
import com.games.games_api.games.mapper.GamesTimestampMapper;
import com.games.games_api.games.repository.GamesRepository;
import com.games.games_api.games.specifications.GameSpecifications;
import com.games.games_api.genres.service.IGenresService;
import com.games.games_api.platforms.service.IPlatformsService;
import com.games.games_api.publishers.service.IPublishersService;
import com.games.games_api.utils.LoggerConstants;

@Service
public class GamesService implements IGamesService {

	private static final Logger logger = LoggerFactory.getLogger(GamesService.class);
	
	private GamesMapper gamesMapper = Mappers.getMapper(GamesMapper.class);
	
	private GamesTimestampMapper gamesMapperTimestamp = Mappers.getMapper(GamesTimestampMapper.class);

	@Autowired
	private GamesRepository gamesRepository;
	
	@Autowired
	private IGenresService genresService;
	
	@Autowired
	private IPlatformsService platformsService;
	
	@Autowired
	private IDevelopersService developersService;
	
	@Autowired
	private IPublishersService publishersService;
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "gamesCache", key = "#id" )
	public CollectionResponse<GameResp> findById(Long id) {
		GameEntity gameEntity = gamesRepository.findById(id).orElse(null);

		if (gameEntity != null) {
			logger.info(LoggerConstants.GENRE_ID_FOUND, gameEntity);
			GameResp response = gamesMapper.toGameResponse(gameEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GameResp> findByCriteria(GameCriteria gameReqCriteria,
			Pageable pageable) {
        Specification<GameEntity> spec = this.initCriteria(gameReqCriteria);
        
        // launch query to find all games via specified criteria
        Page<GameEntity> gamesFound = gamesRepository.findAll(spec, pageable);
        
        // prepare response
        List<GameResp> response = gamesMapper.toGamesResponse(gamesFound.getContent());
        // convert response for pagination
        Page<GameResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());
        
        // the result for platforms, genres,... etc will always give all games that are related to at least one
        // of the corresponding ids in input
        // to have a more specific filter the logic must be implemented else where like in gateway API or FrontEnd
        return new CollectionResponse<>(responsePage);
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GameResp> findByCriteriaDiscovery(GameCriteriaDiscovery gameReqCriteria,
			Pageable pageable) {
        Specification<GameEntity> spec = this.initCriteria(gameReqCriteria);
        
        // launch query to find all games via specified criteria
        Page<GameEntity> gamesFound = gamesRepository.findAll(spec, pageable);
        
        // prepare response
        List<GameResp> response = gamesMapper.toGamesResponse(gamesFound.getContent());
        // convert response for pagination
        Page<GameResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());
        
        // the result for platforms, genres,... etc will always give all games that are related to at least one
        // of the corresponding ids in input
        // to have a more specific filter the logic must be implemented else where like in gateway API or FrontEnd
        return new CollectionResponse<>(responsePage);
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GameResp> findAllByIds(Set<Long> ids) {
		List<GameEntity> allGamesById= gamesRepository.findAllById(ids);
		
		if(!allGamesById.isEmpty()) {
			List<GameResp> response = gamesMapper.toGamesResponse(allGamesById);
			return new CollectionResponse<>(response);
		}
		
        return new CollectionResponse<>();
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<Long> findAllIds() {
		List<Long> allGameIds = gamesRepository.findAllGameIds();
		

        return new CollectionResponse<>(allGameIds);
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GameRespTimestamp> findAllGamesOnPending(Pageable pageable) {
		
		Specification<GameEntity> spec = Specification.where(null);
		
		// STATUS PENDING
		int status = GameStatus.PENDING.getCode();
		spec = spec.and(GameSpecifications.withStatus(status));
		
        // launch query to find all games via specified criteria
        Page<GameEntity> gamesFound = gamesRepository.findAll(spec, pageable);
        
        // prepare response
        List<GameRespTimestamp> response = gamesMapperTimestamp.toGamesResponseTimestamp(gamesFound.getContent());
        // convert response for pagination
        Page<GameRespTimestamp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());

        return new CollectionResponse<>(responsePage);
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GameStatusResp> findAllGamesOnRejected(Pageable pageable) {
		
		Specification<GameEntity> spec = Specification.where(null);
		
		// STATUS REJECTED
		int status = GameStatus.REJECTED.getCode();
		spec = spec.and(GameSpecifications.withStatus(status));
		
        // launch query to find all games via specified criteria
        Page<GameEntity> gamesFound = gamesRepository.findAll(spec, pageable);
        
        // prepare response
        List<GameStatusResp> response = gamesMapper.toGamesResponseStatus(gamesFound.getContent());
        // convert response for pagination
        Page<GameStatusResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());

        return new CollectionResponse<>(responsePage);
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GameStatusResp> findGameStatus(Long id) {
		
        // launch query to find all games via specified criteria
        GameEntity gameFound = gamesRepository.findById(id).orElse(null);
        
        if(gameFound != null) {
            // prepare response
            GameStatusResp response = gamesMapper.toGameResponseStatus(gameFound);
            return new CollectionResponse<>(response);
        }

        return new CollectionResponse<>();
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<Long> getPendingGamesIds(List<Long> gameIds) {
		
		if(gameIds == null || gameIds.isEmpty()) {
			throw new UserReadableException("Field must have at least one element.", "400");
		}
		
		List<Long> response = new ArrayList<>();
		
		// optimize to find byId
		if(gameIds.size() == 1) {
			GameEntity gameFound = gamesRepository.findById(gameIds.get(0)).orElse(null);
			if(gameFound != null && gameFound.getStatus() == 0) {
				response.add(gameFound.getId());
			}
			return new CollectionResponse<>(response);
		}
		
		// look for games
		List<GameEntity> gamesFound = gamesRepository.findAllById(gameIds);
		
		if(!gamesFound.isEmpty()) {
			for(GameEntity gameFound : gamesFound) {
				// only PENDING status is needed
				if(gameFound.getStatus() == 0) {
					response.add(gameFound.getId());
				}
			}
			return new CollectionResponse<>(response);
		}
		
		return new CollectionResponse<>();
	}
	
	
	@Override
	@Transactional
	public CollectionResponse<GameResp> deleteAllGamesByIds(List<Long> gameIds) {
		
		List<GameEntity> entitiesFound = gamesRepository.findAllById(gameIds);
		
		entitiesFound.forEach(el -> {
			if(el.getStatus() != 2) {
				throw new UserReadableException("Error on game deletion: game with ID " + el.getId() + 
						" has status not equal to REJECTED.", "400");
			}
		});
		
		gamesRepository.deleteAllById(gameIds);

        return new CollectionResponse<>();
        
    }
	
	private Specification<GameEntity> initCriteria(GameCriteriaDiscovery gameCriteria) {
		Specification<GameEntity> spec = this.initCriteria((GameCriteria)gameCriteria);
		
		spec = spec.and(GameSpecifications.excludeGameIds(gameCriteria.getExcludedGameIds()));
		
		return spec;
	}
	
	private Specification<GameEntity> initCriteria(GameCriteria gameCriteria) {
		Specification<GameEntity> spec = Specification.where(null);
		
		// STATUS
		// always have to be APPROVED games
		int status = GameStatus.APPROVED.getCode();
		spec = spec.and(GameSpecifications.withStatus(status));
		
		// NAME
		if(gameCriteria.getName() != null) {
			spec = spec.and(GameSpecifications.nameContains(gameCriteria.getName()));
		}
		
		// RELEASE DATE
		Instant minReleaseDate = gameCriteria.getMinReleaseDate();
		
		if(minReleaseDate != null) {

			// default: today
			Instant maxReleaseDate = gameCriteria.getMaxReleaseDate() != null ? gameCriteria.getMaxReleaseDate() : Instant.now(); 
			
			switch(gameCriteria.getReleaseDateFilter()) {
			case GREATER -> spec = spec.and(GameSpecifications.isReleasedAfter(minReleaseDate));
			case MINOR -> spec = spec.and(GameSpecifications.isReleasedBefore(minReleaseDate));
			case EQUAL -> spec = spec.and(GameSpecifications.isReleasedEqual(minReleaseDate));
			case BETWEEN -> spec = spec.and(GameSpecifications
					.isReleasedBetween(minReleaseDate, maxReleaseDate));
			}
			
		}
		
		// STARTING PRICE
		if(gameCriteria.getStartingPrice() != null) {
			Double startingPrice = gameCriteria.getStartingPrice() <= 0 ? 1 : gameCriteria.getStartingPrice();
			
			spec = spec.and(GameSpecifications.isPriceLessThan(startingPrice));
		}
		
		// AVERAGE RATING
		BigDecimal minAverageRating = gameCriteria.getMinAverageRating();
		
		if(minAverageRating != null) {
			
			// default: 10.0
			BigDecimal maxAverageRating = gameCriteria.getMaxAverageRating() != null ? gameCriteria.getMaxAverageRating() : new BigDecimal(10.0); 
			
			switch(gameCriteria.getAverageRatingFilter()) {
			case GREATER -> spec = spec.and(GameSpecifications.hasAvgRatingGreaterThan(minAverageRating));
			case MINOR -> spec = spec.and(GameSpecifications.hasAvgRatingLessThan(minAverageRating));
			case EQUAL -> spec = spec.and(GameSpecifications.hasAvgRatingEqualTo(minAverageRating));
			case BETWEEN -> spec = spec.and(GameSpecifications
					.avgRatingIsBetween(minAverageRating, maxAverageRating));
			}
			
		}
		
		// REVIEW QUANTITY
		Integer minReviewQuantity = gameCriteria.getMinReviewQuantity();
		
		if(minReviewQuantity != null) {
			
			// default: 1000
			Integer maxReviewQuantity = gameCriteria.getMaxReviewQuantity() != null ? gameCriteria.getMaxReviewQuantity() : 1000; 
			
			switch(gameCriteria.getReviewQuantityFilter()) {
			case GREATER -> spec = spec.and(GameSpecifications.hasReviewQuantityGreaterThan(minReviewQuantity));
			case MINOR -> spec = spec.and(GameSpecifications.hasReviewQuantityLessThan(minReviewQuantity));
			case EQUAL -> spec = spec.and(GameSpecifications.hasReviewQuantityEqualTo(minReviewQuantity));
			case BETWEEN -> spec = spec.and(GameSpecifications
					.reviewQuantityIsBetween(minReviewQuantity, maxReviewQuantity));
			}
			
		}
		
        
        // SINGLEPLAYER
        if(gameCriteria.getIsSingleplayer() != null) {
            spec = spec.and(GameSpecifications.isSingleplayer(gameCriteria.getIsSingleplayer()));
        }

        // MULTIPLAYER
        if(gameCriteria.getIsMultiplayer() != null) {
            spec = spec.and(GameSpecifications.isMultiplayer(gameCriteria.getIsMultiplayer()));
        }
        
        // PVP
        if(gameCriteria.getIsPvp() != null) {
            spec = spec.and(GameSpecifications.isPvp(gameCriteria.getIsPvp()));
        }
        
        // PVE
        if(gameCriteria.getIsPve() != null) {
            spec = spec.and(GameSpecifications.isPve(gameCriteria.getIsPve()));
        }
        
        // 2D
        if(gameCriteria.getIsTwoDimensional() != null) {
            spec = spec.and(GameSpecifications.isTwoDimensional(gameCriteria.getIsTwoDimensional()));
        }
        
        // 3D
        if(gameCriteria.getIsThreeDimensional() != null) {
            spec = spec.and(GameSpecifications.isThreeDimensional(gameCriteria.getIsThreeDimensional()));
        }
        
        // GENRES
        if(!CollectionUtils.isEmpty(gameCriteria.getGenreIds())) {
        	spec = spec.and(GameSpecifications.hasGenres(gameCriteria.getGenreIds()));
        }
        
        // PUBLISHERS
        if(!CollectionUtils.isEmpty(gameCriteria.getPublisherIds())) {
        	spec = spec.and(GameSpecifications.hasPublishers(gameCriteria.getPublisherIds()));
        }
        
        // DEVELOPERS
        if(!CollectionUtils.isEmpty(gameCriteria.getDeveloperIds())) {
        	spec = spec.and(GameSpecifications.hasDevelopers(gameCriteria.getDeveloperIds()));
        }
        
        // PLATFORMS
        if(!CollectionUtils.isEmpty(gameCriteria.getPlatformIds())) {
            spec = spec.and(GameSpecifications.hasPlatforms(gameCriteria.getPlatformIds()));
        }
		
		return spec;
	}
	
	@Override
	@Transactional
	public CollectionResponse<GameStatusResp> patchStatus(List<GameReqPatchStatus> gameReqPatchStatusList) {
		// handle the case of single entry
		if(gameReqPatchStatusList.size() == 1) {
			GameReqPatchStatus request = gameReqPatchStatusList.get(0);
			GameEntity entityFound = gamesRepository.findById(request.getId()).orElse(null);
			if(entityFound == null) {
				throw new UserReadableException("No game found.", "404");
			}
			
			int status = request.getStatus();
			entityFound.setStatus(status);
			
			// patch game
			entityFound = gamesRepository.save(entityFound);
			
			// convert to response
			GameStatusResp response = gamesMapper.toGameResponseStatus(entityFound);
			
			return new CollectionResponse<>(response);
		}
		
		Map<Long, Integer> idStatusMap = new HashMap<>();
		List<Long> gameIds = new ArrayList<>();
		
		gameReqPatchStatusList.forEach(el ->  {
			
			idStatusMap.put(el.getId(), el.getStatus());
			gameIds.add(el.getId());
		}
		);
		
		// convert request to entity
		List<GameEntity> entitiesFound = gamesRepository.findAllById(gameIds);
		
		if(entitiesFound.isEmpty()) {
			throw new UserReadableException("No game found.", "404");
		}
		
		for(GameEntity entity : entitiesFound) {
			int status = idStatusMap.get(entity.getId());
			entity.setStatus(status);
		}
		
		// patch game
		entitiesFound = gamesRepository.saveAll(entitiesFound);
		
		// convert to response
		List<GameStatusResp> response = gamesMapper.toGamesResponseStatus(entitiesFound);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	public CollectionResponse<GameResp> create(GameReqPost gameReqPost) {
		
		// convert request to entity
		GameEntity entity = gamesMapper.toGameEntity(gameReqPost);
		
		// by default on creation
		entity.setStatus(0); // PENDING needs approval
		
		// set genres
		if(gameReqPost.getGenreIds() != null) {
			entity.setGenres(genresService.findAllById(gameReqPost.getGenreIds()));
		}
		// set platforms
		if(gameReqPost.getPlatformIds() != null) {
			entity.setPlatforms(platformsService.findAllById(gameReqPost.getPlatformIds()));
		}
		// set developers
		if(gameReqPost.getDeveloperIds() != null) {
			entity.setDevelopers(developersService.findAllById(gameReqPost.getDeveloperIds()));
		}
		// set publishers
		if(gameReqPost.getPublisherIds() != null) {
			entity.setPublishers(publishersService.findAllById(gameReqPost.getPublisherIds()));
		}
		
		// init average rating
		entity.setAverageRating(new BigDecimal(0.00));
		// init review quantity
		entity.setReviewQuantity(0);
		
		// create game
		entity = gamesRepository.save(entity);
		
		// convert to response
		GameResp response = gamesMapper.toGameResponse(entity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	//@CacheEvict(value = "gamesCache", key = "#id")
	@Transactional
	public CollectionResponse<GameResp> update(Long id, GameReqPut gameReqPut) {
		// game must exist
		GameEntity entity = gamesRepository.findById(id).orElseThrow();
		
		// convert request to entity
		gamesMapper.toGameEntity(gameReqPut, entity);
		
		// by default on update
		entity.setStatus(1); // APPROVED doesn't need approval
		
		// for all the correlated entities there will always be a total update and not a partial update.
		// Example: this means that passing an empty list it's considered like complete deletion 
		// from the bridge table.
		
		// set genres
		if(gameReqPut.getGenreIds() != null) {
			entity.setGenres(genresService.findAllById(gameReqPut.getGenreIds()));
		}
		// set platforms
		if(gameReqPut.getPlatformIds() != null) {
			entity.setPlatforms(platformsService.findAllById(gameReqPut.getPlatformIds()));
		}
		// set developers
		if(gameReqPut.getDeveloperIds() != null) {
			entity.setDevelopers(developersService.findAllById(gameReqPut.getDeveloperIds()));
		}
		// set publishers
		if(gameReqPut.getPublisherIds() != null) {
			entity.setPublishers(publishersService.findAllById(gameReqPut.getPublisherIds()));
		}
		
		// update game
		entity = gamesRepository.save(entity);
		
		// convert to response
		GameResp response = gamesMapper.toGameResponse(entity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	//@CacheEvict(value = "gamesCache", key = "#id")
	@Transactional
	public CollectionResponse<GameResp> patch(Long id, GameReqPatch gameReqPatch) {
		
		// game must exist
		GameEntity entity = gamesRepository.findById(id).orElseThrow();
		
		// handle average rating variable
		BigDecimal averageRating = gameReqPatch.getAverageRating();
		if(averageRating != null) {
			averageRating = averageRating.setScale(2, RoundingMode.HALF_UP);
			gameReqPatch.setAverageRating(averageRating);
		}
		
		// convert request to entity
		gamesMapper.toGameEntity(gameReqPatch, entity);
		
		// update game
		entity = gamesRepository.save(entity);
		
		// convert to response
		GameResp response = gamesMapper.toGameResponse(entity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	//@CacheEvict(value = "gamesCache", key = "#id")
	@Transactional
	public CollectionResponse<GameResp> deleteById(Long id) {
		// game must exist
		gamesRepository.findById(id).orElseThrow();
		
		// delete the game
		gamesRepository.deleteById(id);
		
		return new CollectionResponse<>();
	}

}

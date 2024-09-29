package com.games.games_api.updatable_games.service;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.games.games_api.custom.exception.UserReadableException;
import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.games.entity.GameEntity;
import com.games.games_api.games.repository.GamesRepository;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPost;
import com.games.games_api.updatable_games.dto.UpdatableGameReqPut;
import com.games.games_api.updatable_games.dto.UpdatableGameResp;
import com.games.games_api.updatable_games.entity.UpdatableGameEntity;
import com.games.games_api.updatable_games.mapper.UpdatableGamesMapper;
import com.games.games_api.updatable_games.repository.UpdatableGameRepository;
import com.games.games_api.utils.LoggerConstants;

@Service
public class UpdatableGameService implements IUpdatableGameService {
	
	private static final Logger logger = LoggerFactory.getLogger(UpdatableGameService.class);

    @Autowired
    private GamesRepository gamesRepository;
	
    @Autowired
    private UpdatableGameRepository updatableGameRepository;
    
    private UpdatableGamesMapper updatableGamesMapper = Mappers.getMapper(UpdatableGamesMapper.class);
    
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "gamesCache", key = "#id" )
	public CollectionResponse<UpdatableGameResp> findById(Long id) {
		UpdatableGameEntity gameEntity = updatableGameRepository.findById(id).orElse(null);

		if (gameEntity != null) {
			logger.info(LoggerConstants.GENRE_ID_FOUND, gameEntity);
			UpdatableGameResp response = updatableGamesMapper.toUpdatableGameResponse(gameEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<UpdatableGameResp> findAll(Pageable pageable) {
        // launch query to find all games via specified criteria
        Page<UpdatableGameEntity> gamesFound = updatableGameRepository.findAll(pageable);
        // prepare response
        List<UpdatableGameResp> response = updatableGamesMapper.toUpdatableGamesResponse(gamesFound.getContent());
        // convert response for pagination
        Page<UpdatableGameResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());

        return new CollectionResponse<>(responsePage);
    }
	
	@Override
	public CollectionResponse<UpdatableGameResp> create(UpdatableGameReqPost gameReqPost) {
		
		// first find existing game
		GameEntity gameFound = gamesRepository.findById(gameReqPost.getId()).orElseThrow();
		
		// if the updatable game already exist don't proceed
		// updatable game must exist
		UpdatableGameEntity isEntityFound = updatableGameRepository.findById(gameReqPost.getId()).orElse(null);
		
		if(isEntityFound != null) {
			throw new UserReadableException("Updatable game already exists", "400");
		}
		
		UpdatableGameEntity entity = new UpdatableGameEntity();
		
		// first convert the game entity fields to updatable ones
		updatableGamesMapper.fromGameEntity(gameFound, entity);
		// then put the updatable fields to the updatable entity
		updatableGamesMapper.toUpdatableGameEntity(gameReqPost, entity);
		
		
		// associate the game entity to the updatable entity
		entity.setGame(gameFound);
		gameFound.setUpdatableGame(entity);
		
		// save it on games first
		gamesRepository.save(gameFound);
		
		// then create updatable game
		entity = updatableGameRepository.save(entity);
		
		// convert to response
		UpdatableGameResp response = updatableGamesMapper.toUpdatableGameResponse(entity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	//@CacheEvict(value = "gamesCache", key = "#id")
	@Transactional
	public CollectionResponse<UpdatableGameResp> update(Long id, UpdatableGameReqPut gameReqPut) {
		// updatable game must exist
		UpdatableGameEntity entity = updatableGameRepository.findById(id).orElseThrow();
		
		// convert request to entity
		updatableGamesMapper.toUpdatableGameEntity(gameReqPut, entity);
		
		// update game
		entity = updatableGameRepository.save(entity);
		
		// convert to response
		UpdatableGameResp response = updatableGamesMapper.toUpdatableGameResponse(entity);
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional
	public CollectionResponse<UpdatableGameResp> deleteById(Long id) {
		this.deleteGameById(id);
		
		return new CollectionResponse<>();
	}
	
	@Transactional
	private void deleteGameById(Long id) {
		// game must exist
		UpdatableGameEntity entityFound = updatableGameRepository.findById(id).orElseThrow();
		
		// find existing game
		GameEntity gameFound = gamesRepository.findById(id).orElseThrow();
		
		gameFound.setUpdatableGame(null);
		entityFound.setGame(null);
		
		// delete the game
		updatableGameRepository.delete(entityFound);
	}
	
	@Override
	@Transactional
	public CollectionResponse<UpdatableGameResp> deleteAllByIds(List<Long> ids) {
		
		for(Long id : ids) {
			this.deleteGameById(id);
		}
		
		return new CollectionResponse<>();
	}
}

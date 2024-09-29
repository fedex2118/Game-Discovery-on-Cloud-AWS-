package com.users.user_preferences_api.library.service;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.library.dto.LibraryReqPost;
import com.users.user_preferences_api.library.dto.LibraryResp;
import com.users.user_preferences_api.library.entity.LibraryEntity;
import com.users.user_preferences_api.library.entity.LibraryEntityKey;
import com.users.user_preferences_api.library.mapper.LibraryMapper;
import com.users.user_preferences_api.library.repository.LibraryRepository;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class LibraryService implements ILibraryService {
	
	private static final Logger logger = LoggerFactory.getLogger(LibraryService.class);
	
	@Autowired
	private LibraryRepository libraryRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private LibraryMapper libraryMapper = Mappers.getMapper(LibraryMapper.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public CollectionResponse<LibraryResp> create(LibraryReqPost libraryReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(libraryReqPost.getUserId())
				.orElseThrow();
		
		// save the entity
		LibraryEntity libraryEntity = new LibraryEntity();
		
		LibraryEntityKey key = new LibraryEntityKey();
		key.setGameId(libraryReqPost.getGameId());
		key.setUserId(libraryReqPost.getUserId());
		
		libraryEntity.setId(key);
		libraryEntity.setUserPreference(userPreference);
		
		libraryEntity = libraryRepository.save(libraryEntity);
		
		LibraryResp response = new LibraryResp();
		response.setUserId(libraryEntity.getId().getUserId());
		response.setGameId(libraryEntity.getId().getGameId());
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "libraryCache", key = "#gameId + '_' + #userId" )
	public CollectionResponse<LibraryResp> findByKey(Long gameId, Long userId) {
		LibraryEntityKey key = new LibraryEntityKey();
		key.setGameId(gameId);
		key.setUserId(userId);
		
		LibraryEntity libraryFound = libraryRepository.findById(key).orElse(null);
		
		if(libraryFound != null) {
			logger.info(LoggerConstants.LIBRARY_GAME_ID_FOUND, libraryFound.getId());
			LibraryResp response = libraryMapper.toLibraryResponse(libraryFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<LibraryResp> findAllByUserId(Long userId, Pageable pageable) {
		Page<LibraryEntity> gamesFound = libraryRepository.findAllByIdUserId(userId, pageable);
		
		if(gamesFound != null) {
			//logger.info(LoggerConstants._ID_FOUND, Found.getId());
			List<LibraryResp> response = libraryMapper.toLibrariesResponse(gamesFound.getContent());
			
	        // convert it for pagination
	        Page<LibraryResp> responsePage = new PageImpl<>(response, pageable, gamesFound.getTotalElements());
			return new CollectionResponse<>(responsePage);
		}
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "libraryCache", key = "#gameId + '_' + #userId")
	public CollectionResponse<LibraryResp> deleteByKey(Long gameId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		LibraryEntityKey key = new LibraryEntityKey();
		key.setGameId(gameId);
		key.setUserId(userId);
		
		libraryRepository.deleteById(key);
		
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<LibraryResp> deleteUserGames(Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		// first find all games for the input user
		List<LibraryEntity> gamesFound = libraryRepository.findAllByIdUserId(userId);
		
		// evict entries of that specific user
//		for (LibraryEntity game : gamesFound) {
//	        cacheManager.getCache("libraryCache").evict(game.getId() + "_" + userId);
//	    }
		
		// delete all found game entries
		libraryRepository.deleteAll(gamesFound);
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<LibraryResp> deleteGames(Long gameId) {

	    // first find all games with input ID
	    List<LibraryEntity> gamesFound = libraryRepository.findAllByIdGameId(gameId);

	    // delete all found game entries
	    if (!gamesFound.isEmpty()) {
			// evict entries of that specific user
//			for (LibraryEntity game : gamesFound) {
//		        cacheManager.getCache("libraryCache").evict(gameId + "_" + game.getId().getUserId());
//		    }
	    	
	    	libraryRepository.deleteAll(gamesFound);
	    }
	    
	    return new CollectionResponse<>();
	}

}

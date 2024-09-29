package com.users.user_preferences_api.genres.service;

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
import com.users.user_preferences_api.genres.dto.GenreReqPost;
import com.users.user_preferences_api.genres.dto.GenreResp;
import com.users.user_preferences_api.genres.entity.GenreEntity;
import com.users.user_preferences_api.genres.entity.GenreEntityKey;
import com.users.user_preferences_api.genres.mapper.GenresMapper;
import com.users.user_preferences_api.genres.repository.GenresRepository;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;
import com.users.user_preferences_api.preferences.repository.PreferencesRepository;
import com.users.user_preferences_api.utils.LoggerConstants;

@Service
public class GenresService implements IGenresService {
	
	private static final Logger logger = LoggerFactory.getLogger(GenresService.class);
	
	@Autowired
	private GenresRepository genresRepository;
	
	@Autowired
	private PreferencesRepository preferencesRepository;
	
	private GenresMapper genresMapper = Mappers.getMapper(GenresMapper.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public CollectionResponse<GenreResp> create(GenreReqPost genreReqPost) {
		
		// find the existing user
		PreferenceEntity userPreference = preferencesRepository.findById(genreReqPost.getUserId())
				.orElseThrow();
		
		// save the entity
		GenreEntity genreEntity = new GenreEntity();
		
		GenreEntityKey key = new GenreEntityKey();
		key.setGenreId(genreReqPost.getGenreId());
		key.setUserId(genreReqPost.getUserId());
		
		genreEntity.setId(key);
		genreEntity.setUserPreference(userPreference);
		
		genreEntity = genresRepository.save(genreEntity);
		
		GenreResp response = new GenreResp();
		response.setUserId(genreEntity.getId().getUserId());
		response.setGenreId(genreEntity.getId().getGenreId());
		
		return new CollectionResponse<>(response);
	}
	
	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "genresCache", key = "#genreId + '_' + #userId" )
	public CollectionResponse<GenreResp> findByKey(Long genreId, Long userId) {
		GenreEntityKey key = new GenreEntityKey();
		key.setGenreId(genreId);
		key.setUserId(userId);
		
		GenreEntity genreFound = genresRepository.findById(key).orElse(null);
		
		if(genreFound != null) {
			logger.info(LoggerConstants.GENRE_ID_FOUND, genreFound.getId());
			GenreResp response = genresMapper.toGenreResponse(genreFound);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GenreResp> findAllByUserId(Long userId, Pageable pageable) {
		Page<GenreEntity> genresFound = genresRepository.findAllByIdUserId(userId, pageable);
		
		if(genresFound != null) {
			//logger.info(LoggerConstants._ID_FOUND, Found.getId());
			List<GenreResp> response = genresMapper.toGenresResponse(genresFound.getContent());
			
	        // convert it for pagination
	        Page<GenreResp> responsePage = new PageImpl<>(response, pageable, genresFound.getTotalElements());
			return new CollectionResponse<>(responsePage);
		}
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "genresCache", key = "#genreId + '_' + #userId")
	public CollectionResponse<GenreResp> deleteByKey(Long genreId, Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		GenreEntityKey key = new GenreEntityKey();
		key.setGenreId(genreId);
		key.setUserId(userId);
		
		genresRepository.deleteById(key);
		
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<GenreResp> deleteUserGenres(Long userId) {
		// the user must exist
		preferencesRepository.findById(userId).orElseThrow();
		
		// first find all genres for the input user
		List<GenreEntity> genresFound = genresRepository.findAllByIdUserId(userId);
		
		// evict entries of that specific user
//		for (GenreEntity genre : genresFound) {
//	        cacheManager.getCache("genresCache").evict(genre.getId() + "_" + userId);
//	    }
		
		// delete all found genre entries
		genresRepository.deleteAll(genresFound);
		
		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	public CollectionResponse<GenreResp> deleteGenres(Long genreId) {

	    // first find all genres with input ID
	    List<GenreEntity> genres = genresRepository.findAllByIdGenreId(genreId);

	    // delete all found genre entries
	    if (!genres.isEmpty()) {
			// evict entries of that specific user
//			for (GenreEntity genre : genres) {
//		        cacheManager.getCache("genresCache").evict(genreId + "_" + genre.getId().getUserId());
//		    }
	    	
	    	genresRepository.deleteAll(genres);
	    }
	    
	    return new CollectionResponse<>();
	}

}

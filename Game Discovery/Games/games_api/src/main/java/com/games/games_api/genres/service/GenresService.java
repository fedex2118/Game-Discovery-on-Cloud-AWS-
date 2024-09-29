package com.games.games_api.genres.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.games.games_api.custom.response.CollectionResponse;
import com.games.games_api.genres.dto.GenreReqPatch;
import com.games.games_api.genres.dto.GenreReqPost;
import com.games.games_api.genres.dto.GenreResp;
import com.games.games_api.genres.entity.GenreEntity;
import com.games.games_api.genres.mapper.GenresMapper;
import com.games.games_api.genres.repository.GenresRepository;
import com.games.games_api.utils.LoggerConstants;

@Service
public class GenresService implements IGenresService {

	private static final Logger logger = LoggerFactory.getLogger(GenresService.class);

	@Autowired
	private GenresRepository genresRepository;
	
	private GenresMapper genresMapper = Mappers.getMapper(GenresMapper.class);

	@Override
	@Transactional(readOnly = true)
	//@Cacheable(value = "genresCache", key = "#id" )
	public CollectionResponse<GenreResp> findById(Long id) {
		GenreEntity genreEntity = genresRepository.findById(id).orElse(null);

		if (genreEntity != null) {
			logger.info(LoggerConstants.GENRE_ID_FOUND, genreEntity);
			GenreResp response = genresMapper.toGenreResponse(genreEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}

	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GenreResp> findByName(String name) {
		GenreEntity genreEntity = genresRepository.findByName(name).orElse(null);

		if (genreEntity != null) {
			logger.info(LoggerConstants.GENRE_ID_FOUND, genreEntity);
			GenreResp response = genresMapper.toGenreResponse(genreEntity);
			return new CollectionResponse<>(response);
		}

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<GenreEntity> findAllById(Set<Long> ids) {
		// find genres
		Set<GenreEntity> genres = new HashSet<>();
		
		genres.addAll(genresRepository.findAllById(ids));
		
		return genres;
	}
	
	@Override
	@Transactional(readOnly = true)
	public CollectionResponse<GenreResp> findAll(Pageable pageable) {
		Page<GenreEntity> publishersFound =  genresRepository.findAll(pageable);
		
		// prepare response
		List<GenreResp> response = genresMapper.toGenresResponse(publishersFound.getContent());
		
		// convert response for pagination
		Page<GenreResp> responsePage = new PageImpl<>(response, pageable, publishersFound.getTotalElements()); 
		
		return new CollectionResponse<>(responsePage);
	}

	@Override
	public CollectionResponse<GenreResp> create(GenreReqPost genreReqPost) {
		GenreEntity genreEntity = new GenreEntity();
		genreEntity.setName(genreReqPost.getName());

		genreEntity = genresRepository.save(genreEntity);

		GenreResp response = genresMapper.toGenreResponse(genreEntity);
		logger.info(LoggerConstants.GENRE_CREATED, genreEntity);

		return new CollectionResponse<>(response);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "genresCache", key = "#id")
	public CollectionResponse<GenreResp> deleteById(Long id) {
		GenreEntity genreEntity = genresRepository.findById(id).orElseThrow();

		genresRepository.deleteById(id);

		logger.info(LoggerConstants.GENRE_DELETED, genreEntity);

		return new CollectionResponse<>();
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "genresCache", allEntries = true)
	public CollectionResponse<GenreResp> deleteByName(String name) {
		GenreEntity genreEntity = genresRepository.findByName(name).orElseThrow();

		genresRepository.deleteById(genreEntity.getId());;

		logger.info(LoggerConstants.GENRE_DELETED, genreEntity);

		return new CollectionResponse<>();
	}
	
	@Override
	//@CacheEvict(value = "genresCache", key = "#id")
	public CollectionResponse<GenreResp> updateById(Long id, GenreReqPatch genreReqPatch) {
		GenreEntity genreEntity = genresRepository.findById(id).orElseThrow();
		genreEntity.setName(genreReqPatch.getName());
		genreEntity = genresRepository.save(genreEntity);

		GenreResp response = genresMapper.toGenreResponse(genreEntity);
		logger.info(LoggerConstants.GENRE_UPDATED, genreEntity);

		return new CollectionResponse<>(response);
	}
}

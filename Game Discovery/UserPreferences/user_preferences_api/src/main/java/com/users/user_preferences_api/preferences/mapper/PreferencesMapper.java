package com.users.user_preferences_api.preferences.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.util.CollectionUtils;

import com.users.user_preferences_api.created_developers.entity.CreatedDeveloperEntity;
import com.users.user_preferences_api.created_games.entity.CreatedGameEntity;
import com.users.user_preferences_api.created_publishers.entity.CreatedPublisherEntity;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPost;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPut;
import com.users.user_preferences_api.preferences.dto.PreferenceResp;
import com.users.user_preferences_api.preferences.entity.PreferenceEntity;

@Mapper
public interface PreferencesMapper {

	PreferenceEntity toEntity(PreferenceReqPost preferenceReqPost);
	
	@Mapping(target = "createdDeveloperIds", source = "createdDevelopers", qualifiedByName="mapCreatedDevelopers")
	@Mapping(target = "createdPublisherIds", source = "createdPublishers", qualifiedByName="mapCreatedPublishers")
	@Mapping(target = "createdGamesIds", source = "createdGames", qualifiedByName="mapCreatedGames")
	PreferenceResp toResponse(PreferenceEntity entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void toPreferenceEntity(PreferenceReqPut gameReqPut, @MappingTarget PreferenceEntity entity);
	
	@Named(value = "mapCreatedGames")
	default Set<Long> mapCreatedGames(Set<CreatedGameEntity> createdGames) {
		if(CollectionUtils.isEmpty(createdGames)) {
			return new HashSet<>();
		}
		return createdGames.stream().map(el -> el.getGameId()).collect(Collectors.toSet());
		
		
	}
	
	@Named(value = "mapCreatedDevelopers")
	default Set<Long> mapCreatedDevelopers(Set<CreatedDeveloperEntity> createdDevelopers) {
		if(CollectionUtils.isEmpty(createdDevelopers)) {
			return new HashSet<>();
		}
		return createdDevelopers.stream().map(el -> el.getDeveloperId()).collect(Collectors.toSet());
		
		
	}
	
	@Named(value = "mapCreatedPublishers")
	default Set<Long> mapCreatedPublishers(Set<CreatedPublisherEntity> createdPublishers) {
		if(CollectionUtils.isEmpty(createdPublishers)) {
			return new HashSet<>();
		}
		return createdPublishers.stream().map(el -> el.getPublisherId()).collect(Collectors.toSet());
		
		
	}
}

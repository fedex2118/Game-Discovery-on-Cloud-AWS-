package com.gateway.gateway_api.users.services;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.cache.UserSynchService;
import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.response.Message;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.custom.utils.ErrorMessageUtils;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.games.services.GamesRequesterService;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.reviews.data.classes.ReviewResp;
import com.gateway.gateway_api.reviews.services.ReviewsRequesterService;
import com.gateway.gateway_api.users.data.classes.UserReqPatch;
import com.gateway.gateway_api.users.data.classes.UserResp;

@Service
public class UsersService implements IUsersService {
	
	private static final Logger logger = LoggerFactory.getLogger(UsersService.class);
	
	private ObjectWriter objectWriter = new ObjectMapper().registerModule(new JavaTimeModule()).writer();

	@Autowired
	private UsersRequesterService usersRequesterService;
	
	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private ReviewsRequesterService reviewsRequesterService;
	
	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	@Autowired
	private UserSynchService userSynchService;
	
	@Override
	public CollectionResponse<UserResp> getId() {
		
		CollectionResponse<UserResp> response = CustomUtils.getUserIdCollection(usersRequesterService);
		
		return response;
	}
	
	@Override
	public CollectionResponse<UserResp> findById(Long id) {
		
		// check if user is authorized
		CustomUtils.authorize(id, usersRequesterService);
		
		CollectionResponse<UserResp> response = usersRequesterService.findUserById(id);
		
		return response;
	}
	
	@Override
	public CollectionResponse<UserResp> updateById(Long id, UserReqPatch userReqPatch) {
		
		// check if user is authorized
		CustomUtils.authorize(id, usersRequesterService);
		
		CollectionResponse<UserResp> response = usersRequesterService.updateUserById(id, userReqPatch);
		
		return response;
	}
	
	@Override
	public CollectionResponse<UserResp> deleteUserByEmail(String email) {
		
		// check that the user with this email actually exists
		CollectionResponse<UserResp> userRespId = CustomUtils.getUserIdCollection(usersRequesterService);
		
		if(userRespId.getContent().isEmpty()) {
			throw new UserReadableException("User not found", "404");
		}
		
		// no need to check if the user is authorized
		// the authority service already does that for us
		// first delete the user persistent entity
		CollectionResponse<UserResp> response = usersRequesterService.deleteUserByEmail(email);
		
		// handle errors
		if (response.getContent().isEmpty() && !response.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(response);
		}
		
		// for consistency we need to delete preferences and reviews
		// the user already was deleted, we now have garbage data on other dbs
		// by logging reviews and preferences found for the user
		// we can guarantee the data is eliminated via logging / scheduling operations
		// or as a last resource manually deleting those values on the dbs
		// but no recovery it's needed.
		
		Long userId = userRespId.getContent().stream().findFirst().orElse(null).getId();
		
		boolean isDeletionCompletedWithoutErrors = true;
		
		// delete user reviews
		// retrieve all the user reviews
		Pageable pageable = Pageable.ofSize(Integer.MAX_VALUE);
		MultiValueMap<String, String> params = CustomUtils.convertParams(null, pageable);
		
		CollectionResponse<ReviewResp> allUserReviews = reviewsRequesterService.findAllByUserId(userId, params);
		
		Collection<Message> errorMessages = response.getMessages();
		
		// handle errors
		if (allUserReviews.getContent().isEmpty() && !allUserReviews.getMessages().isEmpty()) {
			errorMessages.addAll(allUserReviews.getMessages());
			isDeletionCompletedWithoutErrors = false;
		}
		
		try {
			String allUserReviewsString = objectWriter.writeValueAsString(allUserReviews);
			logger.info("Deletion in progress - User reviews: {}", allUserReviewsString);
		} catch (JsonProcessingException e) {
			logger.error("Deletion in progress - User reviews error {}", e.getMessage());
			e.printStackTrace();
		}
		
		CollectionResponse<ReviewResp> deleteReviewResp = reviewsRequesterService.deleteAll(userId);
		
		// handle errors
		if (deleteReviewResp.getContent().isEmpty() && !deleteReviewResp.getMessages().isEmpty()) {
			errorMessages.addAll(deleteReviewResp.getMessages());
			isDeletionCompletedWithoutErrors = false;
		}
		
		CollectionResponse<PreferenceResp> preferenceResp = preferencesRequesterService.findById(userId);
		
		// handle errors
		if (preferenceResp.getContent().isEmpty() && !preferenceResp.getMessages().isEmpty()) {
			errorMessages.addAll(preferenceResp.getMessages());
			isDeletionCompletedWithoutErrors = false;
		}
		
		if(!preferenceResp.getContent().isEmpty()) {
			// delete the developer and publisher the user may have created
			PreferenceResp preferenceFound = preferenceResp.getContent().stream().findFirst().orElse(null);
			
			Set<Long> developersIds = preferenceFound.getCreatedDeveloperIds();
			Set<Long> publishersIds = preferenceFound.getCreatedPublisherIds();
			
			if(!developersIds.isEmpty()) {
				Long developerId = developersIds.stream().findFirst().orElse(null);
				// delete the developerId
				CollectionResponse<DeveloperResp> result = gamesRequesterService.deleteDeveloperById(developerId);
				
				// handle errors
				if (result.getContent().isEmpty() && !result.getMessages().isEmpty()) {
					errorMessages.addAll(result.getMessages());
					isDeletionCompletedWithoutErrors = false;
				}
			}
			
			if(!developersIds.isEmpty()) {
				Long publisherId = publishersIds.stream().findFirst().orElse(null);
				// delete the publisherId
				CollectionResponse<PublisherResp> result = gamesRequesterService.deletePublisherById(publisherId);
				
				// handle errors
				if (result.getContent().isEmpty() && !result.getMessages().isEmpty()) {
					errorMessages.addAll(result.getMessages());
					isDeletionCompletedWithoutErrors = false;
				}
			}
		}
		
		// the third thing to delete is the user preference
		CollectionResponse<PreferenceResp> deletePreferenceResp = preferencesRequesterService.delete(userId);
		
		// handle errors
		if (deletePreferenceResp.getContent().isEmpty() && !deletePreferenceResp.getMessages().isEmpty()) {
			errorMessages.addAll(deletePreferenceResp.getMessages());
			isDeletionCompletedWithoutErrors = false;
		}
		
		if(!isDeletionCompletedWithoutErrors) {
			// update the concurrentMap for scheduled garbage deletion
			userSynchService.addOrUpdateUserToDeleteId(userId);
			try {
				// delete key from cache
				userSynchService.addKeyToCache(userId);
			} catch (DataAccessException ex) {
	            // Log the exception and proceed without crashing
	        	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
			}
			
		}
		
		return response;
	}
}

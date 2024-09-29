package com.gateway.gateway_api.custom.utils;

import java.lang.reflect.Field;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.games.services.PublishersService;
import com.gateway.gateway_api.users.data.classes.UserResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

/**
 * Class for util methods used on the whole project
 */
public class CustomUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomUtils.class);

	public static UserResp getUserId(UsersRequesterService usersRequesterService) {
		// find the user id
		CollectionResponse<UserResp> userResp = getUserIdCollection(usersRequesterService);

		UserResp userRespId = userResp.getContent().stream().findFirst().orElse(null);
		
		return userRespId;
	}
	
	public static CollectionResponse<UserResp> getUserIdCollection(UsersRequesterService usersRequesterService) {
		// find the user id
		CollectionResponse<UserResp> userResp = usersRequesterService.getId();

		// handle errors
		if (userResp.getContent().isEmpty() && !userResp.getMessages().isEmpty()) {
			ErrorMessageUtils.sendErrorResponse(userResp);
		}
		
		return userResp;
	}
	
	/**
	 * Checks if the user can perform this action by comparing the input id to the user
	 * performing the action (the one on the token).
	 * @return
	 */
	public static UserResp authorize(Long userId, UsersRequesterService usersRequesterService) {

		UserResp userRespId = CustomUtils.getUserId(usersRequesterService);
		
		// check that the user is authorized
		// in the future could be more flexible checking for admin role but it's still a
		// risk
		logger.info("USER REQUEST ID:" + userId);
		logger.info("USER FOUND ID: " + userRespId.getId());
		Boolean simpleComparison = userRespId.getId() == userId;
		logger.info(simpleComparison.toString());
		Boolean equalsComparison = userRespId.getId().equals(userId);
		logger.info(equalsComparison.toString());
		
		if (!equalsComparison) {
			throw new UserReadableException("Unauthorized", "401");
		}
		
		return userRespId;

	}
	
    public static <T> MultiValueMap<String, String> convertParamsFromSupeclass(T criteria) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        if (criteria == null) return params;

        Class<?> currentClass = criteria.getClass();
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(criteria);

                    if (value instanceof Collection) {
                        Collection<?> collection = (Collection<?>) value;
                        for (Object item : collection) {
                            params.add(field.getName(), item.toString());
                        }
                    } else if (value != null) {
                        params.add(field.getName(), value.toString());
                    }
                } catch (IllegalAccessException e) {
                    // Handle exceptions, maybe log them or rethrow as a runtime exception
                    throw new RuntimeException("Error accessing field " + field.getName(), e);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return params;
    }
	
	public static <T> MultiValueMap<String, String>  convertParams(T criteria) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		
	    if (criteria == null) return params;

	    Field[] fields = criteria.getClass().getDeclaredFields();
	    for (Field field : fields) {
	        try {
	            field.setAccessible(true);
	            Object value = field.get(criteria);

	            if (value instanceof Collection) {
	                Collection<?> collection = (Collection<?>) value;
	                for (Object item : collection) {
	                    params.add(field.getName(), item.toString());
	                }
	            } else if (value != null) {
	                params.add(field.getName(), value.toString());
	            }
	        } catch (IllegalAccessException e) {
	            // Handle exceptions, maybe log them or rethrow as a runtime exception
	            throw new RuntimeException("Error accessing field " + field.getName(), e);
	        }
	    }

	    return params;
		
		
	}
	
	public static <T> MultiValueMap<String, String>  convertParams(T criteria, Pageable pageable) {
		MultiValueMap<String, String> params = convertParams(criteria);
		
		if(pageable.isPaged()) {
			params.add("page", pageable.getPageNumber() + "");
			params.add("size", pageable.getPageSize() + "");
			
	        if (pageable.getSort().isSorted()) {
	            pageable.getSort().forEach(order -> {
	                String sorting = order.getProperty() + "," + order.getDirection();
	                params.add("sort", sorting);
	            });
	        }
		}
		
		return params;
	}
	
}

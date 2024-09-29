package com.gateway.gateway_api.custom.scheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.gateway.gateway_api.config.CacheConfig;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.security.JwtService;
import com.gateway.gateway_api.custom.security.TokenService;
import com.gateway.gateway_api.games.data.classes.GameStatusResp;
import com.gateway.gateway_api.games.services.GamesRequesterService;
import com.gateway.gateway_api.games.services.GamesService;
import com.gateway.gateway_api.games.services.UpdatableGameService;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.preferences.services.PreferencesService;
import com.gateway.gateway_api.reviews.services.ReviewsRequesterService;
import com.gateway.gateway_api.reviews.services.ReviewsService;

@Service
public class MainSchedulerService {

	private static final Logger logger = LoggerFactory.getLogger(MainSchedulerService.class);

	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private ReviewsRequesterService reviewsRequesterService;
	
	@Autowired
	private GamesService gamesService;
	
	@Autowired
	private UpdatableGameService updatableGameService;
	
	@Autowired
	private ReviewsService reviewsService;

	@Autowired
	private PreferencesService preferencesService;
	
	@Autowired
	private TokenService tokenService;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Async
	@Scheduled(fixedRate = 180, timeUnit = TimeUnit.MINUTES) // Every 180 minutes = 3 hours
	public void synchronizeGameCacheWithDatabase() {
		logger.info("Scheduled rejected games deletion task initialized at: " + System.currentTimeMillis());
		
		// this scheduler deletes all games that have status REJECTED from games db and
		// user db...
		// it is a massive operation so it must be scheduled with large intervals of time
		
		// this operation may not be done if it has already be done in the last three hours by
		// another container. Let's ask the cache if it has any info about it
		Instant timestamp = null;
		try {
			ValueWrapper valueWrapper = cacheManager.getCache("schedulerGamesDeletion").get("scheduledDeletion");
       	 if(valueWrapper != null) {
       		 Object value = valueWrapper.get();
       		 if(value instanceof String) {
       			timestamp = (Instant) Instant.parse(value.toString());
       		 }
       		 if(value instanceof Instant) {
       			timestamp = (Instant) value;
       		 }
       	 }
       } catch (DataAccessException ex) {
           // Log the exception and proceed without crashing
       	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
       }
		
		if(timestamp != null) {
			// compare the stored timestamp with the current time
			if(timestamp.isAfter(Instant.now().minus(Duration.ofHours(3)))) {
				// this means that no deletion has to be done in this container
				// this because the last update was after three hours ago
				logger.info("Scheduled rejected games deletion task aborted at: " + System.currentTimeMillis());
				return;
			}
		}
		try {
			cacheManager.getCache("schedulerGamesDeletion").put("scheduledDeletion", Instant.now().toString());
		} catch (DataAccessException ex) {
	           // Log the exception and proceed without crashing
	       	logger.error(CacheConfig.REDIS_DOWN_ERROR + ex.getMessage());
		}

		this.setupSecurityContextForAsyncThread();

		// find games on status REJECTED (maximum permitted is 1000)
		CollectionResponse<GameStatusResp> gamesFound = gamesRequesterService.findAllGamesOnRejected();
		
		if(!gamesFound.getContent().isEmpty()) {
			List<Long> ids =  gamesFound.getContent().stream().map(el -> el.getId()).toList();
			
			// need to evict cache entries for every specific cache
			// wipe cache entries
			ids.forEach(id -> {
				gamesService.handleCacheEviction(id); 
				updatableGameService.handleCacheEviction(id);
				// find the user that owns that game
				CollectionResponse<Long> userResp = preferencesRequesterService.findUserIdByOwnedGame(id);
				if(!userResp.getContent().isEmpty()) {
					Long userId = userResp.getContent().stream().findFirst().orElse(null);
					preferencesService.handleCacheEviction(userId);
					// clean also review cache
					reviewsService.handleCacheEviction(id, userId);
				}
			});
			
			// delete all references on preference db
			preferencesRequesterService.deleteGamesWithIds(ids);
			// delete all reviews of those games
			reviewsRequesterService.deleteByGameIds(ids);
			// delete all games
			gamesRequesterService.deleteAllGamesByIds(ids);
			
			// if any errors occur we don't care since this procedure is made to clean garbage data
			// every fixed hours
			
		}
		
		
		logger.info("Scheduled rejected games deletion task finished at: " + System.currentTimeMillis());
	}
	
	private void setupSecurityContextForAsyncThread() {
		String jwt = tokenService.generateToken();

		UserDetails userDetails = jwtService.parseToken(jwt);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, jwt,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}

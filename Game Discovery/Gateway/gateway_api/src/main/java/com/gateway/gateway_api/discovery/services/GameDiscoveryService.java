package com.gateway.gateway_api.discovery.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.gateway.gateway_api.custom.exception.UserReadableException;
import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.custom.utils.CustomUtils;
import com.gateway.gateway_api.games.data.classes.DeveloperResp;
import com.gateway.gateway_api.games.data.classes.GameCriteria.GameCriteriaFilter;
import com.gateway.gateway_api.games.data.classes.GameCriteriaDiscovery;
import com.gateway.gateway_api.games.data.classes.GameResp;
import com.gateway.gateway_api.games.data.classes.GenreResp;
import com.gateway.gateway_api.games.data.classes.PlatformResp;
import com.gateway.gateway_api.games.data.classes.PublisherResp;
import com.gateway.gateway_api.games.services.GamesRequesterService;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;
import com.gateway.gateway_api.preferences.services.PreferencesRequesterService;
import com.gateway.gateway_api.users.data.classes.UserResp;
import com.gateway.gateway_api.users.services.UsersRequesterService;

@Service
public class GameDiscoveryService implements IGameDiscoveryService {
	
	@Autowired
	private PreferencesRequesterService preferencesRequesterService;
	
	@Autowired
	private UsersRequesterService usersRequesterService;
	
	@Autowired
	private GamesRequesterService gamesRequesterService;
	
	private static final Integer LIMIT_OF_RESULTS = 30;
	
	private static final Integer PREFERENCES_BONUS = 100;

	@Override
	public CollectionResponse<GameResp> discover(Double notRandom) {
		
		if(notRandom < 0 || notRandom > 1) {
			throw new UserReadableException("The value of 'notRandom' must be in the range [0, 1].", "400");
		}
		
		UserResp user = CustomUtils.getUserId(usersRequesterService);
		
		CollectionResponse<PreferenceResp> prefResp = preferencesRequesterService.findById(user.getId());
		
		PreferenceResp userPreferences = null;
		
		// initialized the discovery params
		GameCriteriaDiscovery discoveryCriteria = new GameCriteriaDiscovery();
		
		// initialized the search tuning parameters
		Map<Long, Integer> genreCounterMap = new HashMap<>();
		Map<Long, Integer> platformCounterMap = new HashMap<>();
		Map<Long, Integer> publisherCounterMap = new HashMap<>();
		Map<Long, Integer> developerCounterMap = new HashMap<>();
		
		// 0 isSingleplayer, 1 isMultiplayer, 2 isPvp, 3 isPve, 4 is2D, 5 is3D
		int[] booleanCounterArray = new int[6];
		// avgRating
		List<BigDecimal> averageRatingList = new ArrayList<>();
		// reviewQuanity
		// 0, 1...99, 100...499, 500...999, 1000...1999, 2000...3999, 4000...7999, 8000...15999, > 16000
		int[] reviewQuantityArray = new int[9];
		// price
		// 0...10, 11...30, 31...40, 41...50, 51...60, > 61
		int[] priceArray = new int[6];
		// release date
		// 1800 - 1999, 2000 - 2009, 2010 - 2019, 2020 - 2029, > 2030
		int[] dateArray = new int[5];
		
		
		if(!prefResp.getContent().isEmpty() && notRandom != 0) {
			userPreferences = prefResp.getContent().stream().findFirst().orElse(null);
			// if user has some games on his wishlist and on his library
			// this could be usefull information to find better results -> games he could like
			// these games have also to be excluded from the final result
			Set<Long> userCreatedGameIds = userPreferences.getCreatedGamesIds();
			Set<Long> userWishlistGameIds = userPreferences.getWishlistIds();
			Set<Long> userLibraryGameIds = userPreferences.getLibraryIds();
			
			// these are games to be excluded
			discoveryCriteria.setExcludedGameIds(new HashSet<>());
			discoveryCriteria.getExcludedGameIds().addAll(userCreatedGameIds);
			discoveryCriteria.getExcludedGameIds().addAll(userWishlistGameIds);
			discoveryCriteria.getExcludedGameIds().addAll(userLibraryGameIds);
			
			Set<GameResp> userGamesFound = new HashSet<>();
			
			// first look for user created games
			if(!userCreatedGameIds.isEmpty()) {
				this.addGames(userGamesFound, userCreatedGameIds);
			}
			// then look for games he has on wishlist
			if(!userWishlistGameIds.isEmpty()) {
				this.addGames(userGamesFound, userWishlistGameIds);
			}
			// and then look for games he has on library
			if(!userWishlistGameIds.isEmpty()) {
				this.addGames(userGamesFound, userLibraryGameIds);
			}
			
			// of these games we can distinguish some information the user likes most
			for(GameResp gameFound : userGamesFound) {
				// genreCounterMap
				for(GenreResp genre : gameFound.getGenres()) {
					int v = genreCounterMap.getOrDefault(genre.getId(), 0) + 1;
					genreCounterMap.put(genre.getId(), v);
				}
				// platformCounterMap
				for(PlatformResp platform : gameFound.getPlatforms()) {
					int v = platformCounterMap.getOrDefault(platform.getId(), 0) + 1;
					platformCounterMap.put(platform.getId(), v);
				}
				// publisherCounterMap
				for(PublisherResp publisher : gameFound.getPublishers()) {
					int v = publisherCounterMap.getOrDefault(publisher.getId(), 0) + 1;
					publisherCounterMap.put(publisher.getId(), v);
				}
				// developerCounterMap
				for(DeveloperResp developer : gameFound.getDevelopers()) {
					int v = developerCounterMap.getOrDefault(developer.getId(), 0) + 1;
					developerCounterMap.put(developer.getId(), v);
				}
				// 0 isSingleplayer
				if(gameFound.getIsSingleplayer()) {
					booleanCounterArray[0] += 1;
				}
				// 1 isMultiplayer
				if(gameFound.getIsMultiplayer()) {
					booleanCounterArray[1] += 1;
				}
				// 2 isPvp
				if(gameFound.getIsPvp()) {
					booleanCounterArray[2] += 1;
				}
				// 3 isPve
				if(gameFound.getIsPve()) {
					booleanCounterArray[3] += 1;
				}
				// 4 is2D
				if(gameFound.getIsTwoDimensional()) {
					booleanCounterArray[4] += 1;
				}
				// 5 is3D
				if(gameFound.getIsThreeDimensional()) {
					booleanCounterArray[5] += 1;
				}
				
				// avgRating
				averageRatingList.add(gameFound.getAverageRating());
				
				// review quantity
				int reviewQuantity = gameFound.getReviewQuantity();
				// 0
				if(reviewQuantity <= 0) {
					reviewQuantityArray[0] += 1;
				}else if(reviewQuantity <= 99) { // 1...99
					reviewQuantityArray[1] += 1;
				} else if(reviewQuantity <= 499) { // 100...499
					reviewQuantityArray[2] += 1;
				} else if(reviewQuantity <= 999) { // 500...999
					reviewQuantityArray[3] += 1;
				} else if(reviewQuantity <= 1999) { // 1000...1999
					reviewQuantityArray[4] += 1;
				} else if(reviewQuantity <= 3999) { // 2000...3999
					reviewQuantityArray[5] += 1;
				} else if(reviewQuantity <= 7999) { // 4000...7999
					reviewQuantityArray[6] += 1;
				} else if(reviewQuantity <= 15999) { // 8000...15999
					reviewQuantityArray[7] += 1;
				} else { // > 16000
					reviewQuantityArray[8] += 1;
				}
				
				// price
				double price = gameFound.getPrice();
				// 0...10
				if(price <= 10) {
					priceArray[0] += 1;
				} else if(price <= 30) { // 11...30
					priceArray[1] += 1;
				} else if(price <= 40) { // 31...40
					priceArray[2] += 1;
				} else if(price <= 50) { // 41...50
					priceArray[3] += 1;
				} else if(price <= 60) { // 51...60
					priceArray[4] += 1;
				} else { // > 61
					priceArray[5] += 1;
				}
				
				// starting date
				Instant releaseDate = gameFound.getReleaseDate();
				
				int year = LocalDate.ofInstant(releaseDate, ZoneId.systemDefault()).getYear();
				// <= 1999
				if(year <= 1999) {
					dateArray[0] += 1;
				} else if(year <= 2009) { // 2000 - 2009
					dateArray[1] += 1;
				} else if(year <= 2019) { // 2010 - 2019
					dateArray[2] += 1;
				} else if(year <= 2029) { // 2020 - 2029
					dateArray[3] += 1;
				}
				else { // > 2030
					dateArray[4] += 1;
				}
			}
			
			// now for user preferred genres, developers, platforms and publishers
			// since these are user preferences we need to add values to the parameters
			// this is also going to be affected by how much randomness we want
			for(Long id : userPreferences.getGenreIds()) {
				if(userPreferences.getSkipGenres()) {
					int v = genreCounterMap.getOrDefault(id, 0);
					v = notRandom == 0 ? v : v + (int)(PREFERENCES_BONUS / notRandom);
					genreCounterMap.put(id, v);
				}
			}
			for(Long id : userPreferences.getDeveloperIds()) {
				if(userPreferences.getSkipDevelopers()) {
					int v = developerCounterMap.getOrDefault(id, 0);
					v = notRandom == 0 ? v : v + (int)(PREFERENCES_BONUS / notRandom);
					developerCounterMap.put(id, v);
				}
			}
			for(Long id : userPreferences.getPlatformIds()) {
				if(userPreferences.getSkipPlatforms()) {
					int v = platformCounterMap.getOrDefault(id, 0);
					v = notRandom == 0 ? v : v + (int)(PREFERENCES_BONUS / notRandom);
					platformCounterMap.put(id, v);
				}
			}
			for(Long id : userPreferences.getPublisherIds()) {
				if(userPreferences.getSkipPublishers()) {
					int v = publisherCounterMap.getOrDefault(id, 0);
					v = notRandom == 0 ? v : v + (int)(PREFERENCES_BONUS / notRandom);
					publisherCounterMap.put(id, v);
				}
			}
			
			int v = notRandom == 0 ? 0 : (int)(PREFERENCES_BONUS / notRandom);
			
			// 0 isSingleplayer
			if(userPreferences.getIsSingleplayer()) {
				booleanCounterArray[0] += v;
			}
			// 1 isMultiplayer
			if(userPreferences.getIsMultiplayer()) {
				booleanCounterArray[1] += v;
			}
			// 2 isPvp
			if(userPreferences.getIsPvp()) {
				booleanCounterArray[2] += v;
			}
			// 3 isPve
			if(userPreferences.getIsPve()) {
				booleanCounterArray[3] += v;
			}
			// 4 is2D
			if(userPreferences.getIsTwoDimensional()) {
				booleanCounterArray[4] += v;
			}
			// 5 is3D
			if(userPreferences.getIsThreeDimensional()) {
				booleanCounterArray[5] += v;
			}
			
			// avgRating
			// handle avgRating preference
			BigDecimal userAvgRating = userPreferences.getStartingAverageRating();
			if(notRandom == 1) {
				// avgRating user preference needs to be privileged on the search
				for(int i = 0; i < PREFERENCES_BONUS; i++) {
					averageRatingList.add(userAvgRating);
				}
			} else if(notRandom < 1 && notRandom >= 0.5) {
				// avgRating user preference it's going to impact on result
				for(int i = 0; i < (PREFERENCES_BONUS / 2); i++) {
					averageRatingList.add(userAvgRating);
				}
			} else if(notRandom < 0.5 && notRandom > 0) {
				// avgRating it's gonna have some impact on result
				for(int i = 0; i < (PREFERENCES_BONUS / 4); i++) {
					averageRatingList.add(userAvgRating);
				}
			} // else: avgRating will not impact on result
			
			// review quantity
			int reviewQuantity = userPreferences.getStartingReviewQuantity();
			// 0
			if(reviewQuantity <= 0) {
				reviewQuantityArray[0] += v;
			}else if (reviewQuantity <= 99) { // 1...99
				reviewQuantityArray[1] += v;
			} else if(reviewQuantity <= 499) { // 100...499
				reviewQuantityArray[2] += v;
			} else if(reviewQuantity <= 999) { // 500...999
				reviewQuantityArray[3] += v;
			} else if(reviewQuantity <= 1999) { // 1000...1999
				reviewQuantityArray[4] += v;
			} else if(reviewQuantity <= 3999) { // 2000...3999
				reviewQuantityArray[5] += v;
			} else if(reviewQuantity <= 7999) { // 4000...7999
				reviewQuantityArray[6] += v;
			} else if(reviewQuantity <= 15999) { // 8000...15999
				reviewQuantityArray[7] += v;
			} else { // > 16000
				reviewQuantityArray[8] += v;
			}
			
			// price
			double price = userPreferences.getStartingPrice();
			// 0...10
			if(price <= 10) {
				priceArray[0] += v;
			} else if(price <= 30) { // 11...30
				priceArray[1] += v;
			} else if(price <= 40) { // 31...40
				priceArray[2] += v;
			} else if(price <= 50) { // 41...50
				priceArray[3] += v;
			} else if(price <= 60) { // 51...60
				priceArray[4] += v;
			} else { // > 61
				priceArray[5] += v;
			}
			
			// starting date
			Instant releaseDate = userPreferences.getStartingDate();
			
			int year = LocalDate.ofInstant(releaseDate, ZoneId.systemDefault()).getYear();
			// <= 1999
			if(year <= 1999) {
				dateArray[0] += v;
			} else if(year <= 2009) { // 2000 - 2009
				dateArray[1] += v;
			} else if(year <= 2019) { // 2010 - 2019
				dateArray[2] += v;
			} else if(year <= 2029) { // 2020 - 2029
				dateArray[3] += v;
			}
			else { // > 2030
				dateArray[4] += v;
			}
			// initialize discoveryCriteria
			
	        discoveryCriteria.setGenreIds(toTopFive(genreCounterMap));
	        discoveryCriteria.setPlatformIds(toTopFive(platformCounterMap));
	        discoveryCriteria.setPublisherIds(toTopFive(publisherCounterMap));
	        discoveryCriteria.setDeveloperIds(toTopFive(developerCounterMap));
	        
	        int[] userFavouritesBoolean = findBiggestValueIndices(booleanCounterArray);
	        
	        switch(userFavouritesBoolean[0]) { // 0 isSinglePlayer, 1 isMultiplayer
	        case 0 -> discoveryCriteria.setIsSingleplayer(true);
	        case 1 -> discoveryCriteria.setIsMultiplayer(true);
	        // the other value will be null which means that search engine will not care if the
	        // other value is true or false
	        }
	        
	        switch(userFavouritesBoolean[1]) { // 2 isPvp, 3 isPve
	        case 2 -> discoveryCriteria.setIsPvp(true);
	        case 3 -> discoveryCriteria.setIsPve(true);
	        // the other value will be null which means that search engine will not care if the
	        // other value is true or false
	        }
	        
	        switch(userFavouritesBoolean[2]) { // 4 is2D, 5 is3D
	        case 4 -> discoveryCriteria.setIsTwoDimensional(true);
	        case 5 -> discoveryCriteria.setIsThreeDimensional(true);
	        // the other value will be null which means that search engine will not care if the
	        // other value is true or false
	        }
			
	        // avgRating
	        BigDecimal average = calculateAverage(averageRatingList);
	        average = average.multiply(new BigDecimal("0.30"));
	        if(notRandom > 0) {
	        	average =  average.multiply(new BigDecimal(notRandom.toString()));
	        }
	        
	        discoveryCriteria.setAverageRatingFilter(GameCriteriaFilter.GREATER);
	        discoveryCriteria.setMinAverageRating(average);
	        
	        // reviewQuanity
	        discoveryCriteria.setReviewQuantityFilter(GameCriteriaFilter.GREATER);
	        // we pick the greatest sum from the groups
	        // 0 and 1...99, and 100...499, 500...999 and 1000...1999, 2000...3999 and 4000...7999, 8000...15999, > 16000
	        int[] groupsSum = calculateGroupSums(reviewQuantityArray);
	        int winnerGroup = findWinningGroup(groupsSum);
	        
	        switch(winnerGroup) {
	        case 0 ->  {
	        	// don't apply the review quantity filter
		        discoveryCriteria.setReviewQuantityFilter(null);
	        }
	        case 1 -> discoveryCriteria.setMinReviewQuantity(1);
	        case 2 -> discoveryCriteria.setMinReviewQuantity(100);
	        case 3 -> discoveryCriteria.setMinReviewQuantity(1000);
	        case 4 -> discoveryCriteria.setMinReviewQuantity(4000);
	        }
	        
	        // price
	        Integer[] indices = IntStream.range(0, priceArray.length)
	                                     .boxed()
	                                     .toArray(Integer[]::new);

	        // Sort indices based on the values in occurrences array
	        Arrays.sort(indices, (i1, i2) -> Integer.compare(priceArray[i2], priceArray[i1]));


	        // Get the top three indices
	        Integer[] topIndices = Arrays.copyOfRange(indices, 0, 3);
	        // 0...10, 11...30, 31...40, 41...50, 51...60, > 61
	        // Ranges corresponding to each index
	        String[] ranges = {"0-10", "11-30", "31-40", "41-50", "51-60", "61+"};

	        // Compute the midpoint for each top range and assign weights
	        double[] midpoints = new double[topIndices.length];
	        double[] weights = new double[topIndices.length];

	        for (int i = 0; i < topIndices.length; i++) {
	            int index = topIndices[i];
	            String[] parts = ranges[index].split("-");
	            int lowerBound = Integer.parseInt(parts[0]);
	            int upperBound = parts[1].equals("+") ? 100 : Integer.parseInt(parts[1]); // Handle "61+"
	            midpoints[i] = (lowerBound + upperBound) / 2.0;
	            
	            // Assign weights inversely proportional to the range's upper bound
	            weights[i] = 1.0 / (upperBound + 1); // Adding 1 to avoid division by zero
	        }

	        // Apply randomness factor
	        double weightedSum = 0;
	        double weightTotal = 0;
	        for (int i = 0; i < midpoints.length; i++) {
	            double adjustedWeight = weights[i] * (1 - notRandom) + notRandom;
	            weightedSum += midpoints[i] * adjustedWeight;
	            weightTotal += adjustedWeight;
	        }
	        double weightedAveragePrice = weightedSum / weightTotal;
	        
	        discoveryCriteria.setStartingPrice(weightedAveragePrice);
	        
	        // release date
	        // 1800 - 1999, 2000 - 2009, 2010 - 2019, 2020 - 2029, > 2030
	        
	        // only care about the first four positions
	        int[] firstFour = Arrays.copyOfRange(dateArray, 0, 4);

	        // Find the indices of the top two values within the first four positions
	        indices = IntStream.range(0, firstFour.length)
	                                     .boxed()
	                                     .toArray(Integer[]::new);

	        // Sort indices based on the values in the firstFour array in descending order
	        Arrays.sort(indices, (i1, i2) -> Integer.compare(firstFour[i2], firstFour[i1]));

	        // Get the top two indices
	        topIndices = Arrays.copyOfRange(indices, 0, 2);

	        // Randomly pick one of the top two indices based on randomness
	        Random random = new Random();
	        int selectedIndex;
	        if (random.nextDouble() < notRandom) {
	            // Choose the highest value when randomness is high
	            selectedIndex = topIndices[0];
	        } else {
	            // Randomly choose one of the top two values when randomness is low
	            selectedIndex = topIndices[random.nextInt(2)];
	        }
	        
	        discoveryCriteria.setReleaseDateFilter(GameCriteriaFilter.GREATER);
	        
	        // 1800 - 1999, 2000 - 2009, 2010 - 2019, 2020 - 2029, > 2030
	        switch(selectedIndex) {
	        case 0 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(1800, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
	        case 1 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
	        case 2 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(2009, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
	        case 3 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
	        }
	        
		} else {
			// if user has no preferences yet or the search is purely random
	        
			// for this search no genres, publishers etc...
			
			this.randomSearch(discoveryCriteria, booleanCounterArray);
	        
		}
		MultiValueMap<String, String> params = CustomUtils.convertParamsFromSupeclass(discoveryCriteria);
		// send the request
		CollectionResponse<GameResp> response = gamesRequesterService.findGamesDiscovery(params);
		int i = 0;
		while(i < 10) {
			i++;
			if(response.getContent().size() < 10) {
				if(i > 3) {
					// reset the genreIds etc
					discoveryCriteria = resetDiscoveryParamsIds(discoveryCriteria);
				} else {
					// reset only the used params
					discoveryCriteria = resetDiscoveryParams(discoveryCriteria);
				}
				this.randomSearch(discoveryCriteria, booleanCounterArray);
				
				params = CustomUtils.convertParamsFromSupeclass(discoveryCriteria);
				// send another request
				CollectionResponse<GameResp> responseRandom = gamesRequesterService.findGamesDiscovery(params);
				
				Set<GameResp> responseSet = response.getContent().stream().collect(Collectors.toSet());
				responseSet.addAll(responseRandom.getContent());
				
				response.getContent().clear();
				response.getContent().addAll(responseSet);
				
			}
		}
		int responseSize = response.getContent().size();
		
		response.getPaginationDetails().setCurrentPage(0);
		response.getPaginationDetails().setTotalElements(responseSize);
		int totalPages = 0;
		if(!response.getContent().isEmpty()) {
			totalPages = responseSize > 10  ? 2 : 1;
		}
		response.getPaginationDetails().setTotalPages(totalPages);
		// maximum possible amount of results is going to be 9 + 10 = 19
		
		return response;
	}
	
	private GameCriteriaDiscovery resetDiscoveryParams(GameCriteriaDiscovery discoveryCriteria) {
		GameCriteriaDiscovery newDiscoveryCriteria = new GameCriteriaDiscovery();
		newDiscoveryCriteria.setGenreIds(discoveryCriteria.getGenreIds());
		newDiscoveryCriteria.setPlatformIds(discoveryCriteria.getPlatformIds());
		newDiscoveryCriteria.setPublisherIds(discoveryCriteria.getPublisherIds());
		newDiscoveryCriteria.setDeveloperIds(discoveryCriteria.getDeveloperIds());
		newDiscoveryCriteria.setExcludedGameIds(discoveryCriteria.getExcludedGameIds());
		
		return newDiscoveryCriteria;
	}
	
	private GameCriteriaDiscovery resetDiscoveryParamsIds(GameCriteriaDiscovery discoveryCriteria) {
		GameCriteriaDiscovery newDiscoveryCriteria = new GameCriteriaDiscovery();
		newDiscoveryCriteria.setExcludedGameIds(discoveryCriteria.getExcludedGameIds());
		
		return newDiscoveryCriteria;
	}
	
	private void randomSearch(GameCriteriaDiscovery discoveryCriteria, int[] booleanCounterArray) {
		Random random = new Random();
		// choose randomly what values to apply on the search
		for(int i = 0; i < booleanCounterArray.length; i++) {

            // Generate a random integer that is either 0 or 1
            int zeroOneRandom = random.nextInt(2);
            
            boolean isTrue = zeroOneRandom == 0 ? false : true;
            
	        switch(i) {
	        case 0 -> discoveryCriteria.setIsSingleplayer(isTrue);
	        case 1 -> discoveryCriteria.setIsMultiplayer(isTrue);
	        case 2 -> discoveryCriteria.setIsPvp(isTrue);
	        case 3 -> discoveryCriteria.setIsPve(isTrue);
	        case 4 -> discoveryCriteria.setIsTwoDimensional(isTrue);
	        case 5 -> discoveryCriteria.setIsThreeDimensional(isTrue);
	        }
	        
        }
		
		// averageRating

        // Generate a random integer that is either 0 or 1
        int zeroOneRandom = random.nextInt(2);
        
        // randomly pick if average rating is used
        if(zeroOneRandom == 1) {
			// let's pick randomly a number from 0 to 9
            int zeroNineRandom = random.nextInt(10);
        	discoveryCriteria.setAverageRatingFilter(GameCriteriaFilter.GREATER);
        	discoveryCriteria.setMinAverageRating(BigDecimal.valueOf(zeroNineRandom));
        }
        
        // reviewQuanity

        
        // randomly pick if is necessary from the search
        // Generate a random integer that is either 0 or 1
        zeroOneRandom = random.nextInt(2);
        
        // if it's zero randomly pick if it s greater or equal
        if(zeroOneRandom == 1) {
            // randomly pick a value from 0 to 5
            int zeroFiveRandom = random.nextInt(6);
        	if(zeroFiveRandom == 0) {
            	zeroOneRandom = random.nextInt(2);
            	
            	if(zeroOneRandom == 1) {
            		discoveryCriteria.setReviewQuantityFilter(GameCriteriaFilter.GREATER);
            	} else {
            		discoveryCriteria.setReviewQuantityFilter(GameCriteriaFilter.EQUAL);
            	}
            	discoveryCriteria.setMinReviewQuantity(0);
        	} else {
        		// for other values always greather than to maximise results
        		discoveryCriteria.setReviewQuantityFilter(GameCriteriaFilter.GREATER);
        		// pick 1 to 5
        		// 1...99, 100...499, 500...999, 1000...1999, 2000...3999
        		switch(zeroFiveRandom) {
        		case 1 -> discoveryCriteria.setMinReviewQuantity(1);
        		case 2 -> discoveryCriteria.setMinReviewQuantity(100);
        		case 3 -> discoveryCriteria.setMinReviewQuantity(500);
        		case 4 -> discoveryCriteria.setMinReviewQuantity(1000);
        		case 5 -> discoveryCriteria.setMinReviewQuantity(2000);
        		}
        	}

        }
        
		// price
		// 0...10, 11...30, 31...40, 41...50, 51...60, > 61
        
        // Generate a random integer that is either 0 or 1
        zeroOneRandom = random.nextInt(2);
        
        if(zeroOneRandom == 1) {
        	// pick randomly less than starting price from 0 to 4
        	int zeroFourRandom = random.nextInt(5);
        	// search filter  is less then
        	
        	switch(zeroFourRandom) {
    		case 0 -> discoveryCriteria.setStartingPrice(10.0);
    		case 1 -> discoveryCriteria.setStartingPrice(30.0);
    		case 2 -> discoveryCriteria.setStartingPrice(40.0);
    		case 3 -> discoveryCriteria.setStartingPrice(50.0);
    		case 4 -> discoveryCriteria.setStartingPrice(200.0);
        	}
        }
        
		// release date
		// 1800 - 1999, 2000 - 2009, 2010 - 2019, 2020 - 2029, > 2030
        
        // Generate a random integer that is either 0 or 1
        zeroOneRandom = random.nextInt(2);
        
        if(zeroOneRandom == 1) {
        	// pick randomly release date greater than from 0 to 3
        	int zeroThreeRandom = random.nextInt(4);
        	
        	discoveryCriteria.setReleaseDateFilter(GameCriteriaFilter.GREATER);
        	
        	switch(zeroThreeRandom) {
	        case 0 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(1800, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
	        case 1 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
	        case 2 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(2009, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
	        case 3 -> discoveryCriteria.setMinReleaseDate(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
        	}
        }
        
	}
	
    private int[] calculateGroupSums(int[] occurrences) {
        int[] groupSums = new int[5];
        
        // 0
        groupSums[0] = occurrences[0];
        // 1...99
        groupSums[1] = occurrences[1];
        // 100...499, 500...999
        groupSums[2] = occurrences[2] + occurrences[3];
        // 1000...1999, 2000...3999
        groupSums[3] = occurrences[4] + occurrences[5];
        // 4000...7999, 8000...15999, > 16000
        groupSums[4] = occurrences[6] + occurrences[7] + occurrences[8];

        return groupSums;
    }

    private int findWinningGroup(int[] groupSums) {
        int winningGroup = 0;
        int maxSum = groupSums[0];

        for (int i = 1; i < groupSums.length; i++) {
            if (groupSums[i] > maxSum) {
                maxSum = groupSums[i];
                winningGroup = i;
            }
        }

        return winningGroup;
    }
	
    private BigDecimal calculateAverage(List<BigDecimal> list) {
        if (list == null || list.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : list) {
            sum = sum.add(value);
        }

        BigDecimal average = sum.divide(new BigDecimal(list.size()), RoundingMode.HALF_UP);
        return average;
    }
	
    private int[] findBiggestValueIndices(int[] array) {
        int[] biggestIndices = new int[3];

        // look for pairs (isSinglePlayer | isMultiplayer) to find what the user prefers the most ...
        biggestIndices[0] = (array[0] >= array[1]) ? 0 : 1;
        biggestIndices[1] = (array[2] >= array[3]) ? 2 : 3;
        biggestIndices[2] = (array[4] >= array[5]) ? 4 : 5;

        return biggestIndices;
    }
	
	private Set<Long> toTopFive(Map<Long, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // descending order
                .limit(5) // top 5 results
                .map(Entry::getKey)
                .collect(Collectors.toSet());
	}
	
	private void addGames(Set<GameResp> gamesFound, Set<Long> gameIds) {
		Set<Long> gamesToFind = new HashSet<>();
		
		List<Long> gamesList = new ArrayList<>(gameIds);
		if(gameIds.size() > LIMIT_OF_RESULTS) {
			Collections.shuffle(gamesList);
		}
		
		int size = gameIds.size() > LIMIT_OF_RESULTS ? LIMIT_OF_RESULTS : gameIds.size();
		// but with these we may find some precious information on what the user likes
		for(int i = 1; i < size; i++) {
			// retrieve the first random values
			gamesToFind.add(gamesList.get(i));
		}
		
		if(!gamesToFind.isEmpty()) {
			CollectionResponse<GameResp> gamesResp = gamesRequesterService.findGamesByIds(gamesToFind);
			if(!gamesResp.getContent().isEmpty()) {
				gamesFound.addAll(gamesResp.getContent());
			}
		}
	}
}

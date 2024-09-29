package com.users.user_preferences_api.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
	
	private final CacheManager cacheManager;
	
	public CacheConfig(@Lazy CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("preferenceCache", "createdGamesCache",
        		"libraryCache", "wishlistCache", "developersCache", "genresCache",
        		"platformsCache", "publishersCache");
    }
    
    public void evictAllCaches() {
    	cacheManager.getCacheNames().stream()
          .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
    
    // full wipe every 60 minutes
    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.MINUTES)
    public void evictAllcachesAtIntervals() {
        evictAllCaches();
    }
}

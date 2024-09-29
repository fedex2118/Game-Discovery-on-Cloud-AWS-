package com.gateway.gateway_api.custom.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomCacheErrorHandler implements CacheErrorHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomCacheErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        logError(exception, cache, key);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        logError(exception, cache, key);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        logError(exception, cache, key);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        logError(exception, cache, null);
    }

    private void logError(RuntimeException exception, Cache cache, Object key) {
        // log exception details
    	String errorMessage = "Cache operation failed. Cache: " + cache.getName() + ", Key: " + key + ", Exception: " + exception.getMessage();
        System.err.println(errorMessage);
        logger.error(errorMessage);
    }
}

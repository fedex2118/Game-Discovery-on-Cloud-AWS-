package com.gateway.gateway_api.custom.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PreferenceCacheService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final long CACHE_EXPIRATION = 3600; // expiration time in seconds
	
	public void evictPreferenceId(Long userId) {
		redisTemplate.execute((RedisCallback<Object>) connection -> {
			String userPreferenceKey = "preferenceCache::" + userId;

			// start Redis transaction
			connection.multi();

			connection.stringCommands().getDel(userPreferenceKey.getBytes());
			
			// execute transaction
			connection.exec();

			return null;
		});
	}
}

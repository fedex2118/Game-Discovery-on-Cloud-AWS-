package com.gateway.gateway_api.custom.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserSynchService {
	
	private static final String PREFIX = "userToDelete:";

	private final ConcurrentMap<Long, Long> userToDeleteIds = new ConcurrentHashMap<>();	
	private final RedisTemplate<String, String> redisTemplate;

	public UserSynchService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public void addOrUpdateUserToDeleteId(Long userId) {
		userToDeleteIds.put(userId, userId);
    }

    public ConcurrentMap<Long, Long> getUserToDeleteIdMap() {
        return userToDeleteIds;
    }

    public void removeUserToDeleteId(Long userId) {
    	userToDeleteIds.remove(userId);
    }
    
	public void addKeyToCache(Long userId) {
		String key = PREFIX + userId;
		redisTemplate.opsForValue().set(key, userId + "");
	}

	public void removeKeyFromCache(Long userId) {
		String key = PREFIX + userId;
		redisTemplate.delete(key);
	}

	public Set<String> getKeysWithPrefix() {
		return redisTemplate.keys(PREFIX + "*");
	}
	
	public String getValue(Long userId) {
		String key = PREFIX + userId;
		String value = redisTemplate.opsForValue().get(key);
		return value;
	}
}

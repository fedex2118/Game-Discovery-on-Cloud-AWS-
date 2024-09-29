package com.gateway.gateway_api.custom.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameStatsCacheService {

	private static final String PREFIX = "reviewScheduled:";
	private final RedisTemplate<String, String> redisTemplate;
	private final ConcurrentMap<Long, Long> gameIdMap = new ConcurrentHashMap<>();
	private final ConcurrentMap<Long, Long> userToDeleteIds = new ConcurrentHashMap<>();

	public GameStatsCacheService(RedisTemplate<String, String> redisTemplate) {
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
	
	public void addOrUpdateGameId(Long gameId) {
        gameIdMap.put(gameId, System.currentTimeMillis());
    }

    public ConcurrentMap<Long, Long> getGameIdMap() {
        return gameIdMap;
    }

    public void removeGameId(Long gameId) {
        gameIdMap.remove(gameId);
    }

	public void incrementReviewChangeCount(Long gameId) {
		String key = PREFIX + gameId;
		redisTemplate.opsForValue().increment(key);
	}

	public void removeReviewChangeKey(Long gameId) {
		String key = PREFIX + gameId;
		redisTemplate.delete(key);
	}

	public Set<String> getKeysWithPrefix() {
		return redisTemplate.keys(PREFIX + "*");
	}
	
	public String getReviewChangeCountOrNull(Long gameId) {
		String key = PREFIX + gameId;
		String count = redisTemplate.opsForValue().get(key);
		return count;
	}

	public Integer getReviewChangeCount(Long gameId) {
		String key = PREFIX + gameId;
		String count = redisTemplate.opsForValue().get(key);
		return count != null ? Integer.valueOf(count) : 0;
	}
}

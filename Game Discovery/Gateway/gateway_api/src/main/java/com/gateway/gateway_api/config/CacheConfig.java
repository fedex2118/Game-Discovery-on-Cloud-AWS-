package com.gateway.gateway_api.config;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gateway.gateway_api.custom.cache.CustomCacheErrorHandler;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
	
	public static final String REDIS_DOWN_ERROR = "Redis is down, proceeding without cache : ";

    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60)) // set default cache time to live to 60 minutes
            .disableCachingNullValues()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()
            		.configure(objectMapper -> objectMapper.registerModule(new JavaTimeModule()))));

        Set<String> initialCacheNames = new HashSet<String>();
        initialCacheNames.add("preferenceCache");
        initialCacheNames.add("gameCache");
        initialCacheNames.add("reviewCache");
        initialCacheNames.add("updatableGameCache");
        initialCacheNames.add("developerCache");
        initialCacheNames.add("publisherCache");
        
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(cacheConfiguration)
            .initialCacheNames(initialCacheNames)
            .transactionAware()
            .build();
    }
    
    @Bean
    CustomCacheErrorHandler cacheErrorHandler() {
        return new CustomCacheErrorHandler();
    }

}

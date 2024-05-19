package com.neospark.config;

import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	
	@Value("${cache.host}")
	private String cacheHost;
	
	@Value("${cache.port}")
	private int cachePort;
	
	@Bean(name = "redisCacheManager")
	CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		return RedisCacheManagerBuilder
				.fromConnectionFactory(connectionFactory).cacheDefaults( RedisCacheConfiguration.defaultCacheConfig()
	                    .entryTtl(Duration.ofMinutes(30))).build();
	    
	}
	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setConnectionFactory(connectionFactory);

		return template;
	}
	
	
	@Bean
	RedisConnectionFactory getStandAloneConnFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(cacheHost,
				cachePort);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);

	}
}

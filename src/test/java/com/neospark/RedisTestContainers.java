package com.neospark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.neospark.service.CacheService;
import com.redis.testcontainers.RedisContainer;

import lombok.extern.slf4j.Slf4j;

@Testcontainers
@SpringBootTest
@Slf4j
public class RedisTestContainers {

	@Autowired
	private CacheService redisCacheService;

	@Autowired
	private RedisTemplate template;

	@Autowired
	private Environment env;

	@Container
	private static final RedisContainer REDIS_CONTAINER = new RedisContainer(
			DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

	@DynamicPropertySource
	private static void registerRedisProperties(DynamicPropertyRegistry registry) {
		registry.add("cache.host", REDIS_CONTAINER::getHost);
		registry.add("cache.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
	}

	@Test
	void givenRedisContainerConfiguredWithDynamicProperties_whenCheckingRunningStatus_thenStatusIsRunning() {
		assertTrue(REDIS_CONTAINER.isRunning());
	}

	@Test
	public void test10MinTTL() {
		assertEquals(redisCacheService.get10MinTTL(), "Hello World from 10m");
		assertEquals(template.getExpire("10m-cache-ttl::somename",TimeUnit.MINUTES).longValue(),9L);
	}
	
	@Test
	public void test20MinTTL() {
		assertEquals(redisCacheService.get20MinTTL(), "Hello World from 20m");
		assertEquals(template.getExpire("20m-cache-ttl::somename",TimeUnit.MINUTES).longValue(), 19L);
	}
}

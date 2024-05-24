package com.neospark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
public class RedisTestContainerTests {

	@Autowired
	private CacheService redisCacheService;


	@Autowired
	private RedisTemplate<String, ?> template;

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
		long ttlInMin = template.getExpire("10m-cache-ttl::somename",TimeUnit.MINUTES).longValue();
		assertTrue(ttlInMin >= 9 && ttlInMin <=10);
	}
	
	@Test
	public void test20MinTTL() {
		assertEquals(redisCacheService.get20MinTTL(), "Hello World from 20m");
		long ttlInMin = template.getExpire("20m-cache-ttl::somename",TimeUnit.MINUTES).longValue();

		assertTrue(ttlInMin >= 19 && ttlInMin <=20);

	}

}

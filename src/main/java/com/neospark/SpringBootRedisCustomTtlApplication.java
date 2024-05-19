package com.neospark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class })
@EnableCaching
public class SpringBootRedisCustomTtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisCustomTtlApplication.class, args);
	}

}

package com.neospark.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CacheService {
	
    @Cacheable("default-cache")
    public String getData() {
        try {
        	log.warn("Thread sleeping for 5 seconds");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.warn("Thread sleep interrupted");
		}
        return "Hello World";
    }
    
    @Cacheable(value = "10m-cache-ttl",key = "'somename'", cacheResolver = "customCacheResolver")
    public String get10MinTTL() {
        try {
        	log.warn("Thread sleeping for 5 seconds");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.warn("Thread sleep interrupted");
		}
        return "Hello World from 10m";
    }
    
    @Cacheable(value = "20m-cache-ttl",key = "'somename'", cacheResolver = "customCacheResolver")
    public String get20MinTTL() {
        try {
        	log.warn("Thread sleeping for 5 seconds");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.warn("Thread sleep interrupted");
		}
        return "Hello World from 20m";
    }
}

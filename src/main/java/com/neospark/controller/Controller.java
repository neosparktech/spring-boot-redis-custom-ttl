package com.neospark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neospark.service.CacheService;

@RestController
public class Controller {
	
	@Autowired
	CacheService redisCacheService;

	@GetMapping("/get/data")
	public String getData() {
		return redisCacheService.getData();
	}
	
	@GetMapping("/get/datattl/10")
	public String getData10TTL() {
		return redisCacheService.get10MinTTL();
	}
	
	@GetMapping("/get/datattl/20")
	public String getData20TTL() {
		return redisCacheService.get20MinTTL();
	}

}

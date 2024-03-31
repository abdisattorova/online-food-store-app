package com.online.foodstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FoodStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodStoreApplication.class, args);
	}

}

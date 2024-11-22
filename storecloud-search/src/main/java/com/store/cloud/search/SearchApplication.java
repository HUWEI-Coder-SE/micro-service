package com.store.cloud.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author FrozenWatermelon
 * @date 2020/09/24
 */
@SpringBootApplication(scanBasePackages = { "com.store.cloud" })
@EnableFeignClients(basePackages = {"com.store.cloud.api.**.feign"})
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}

}

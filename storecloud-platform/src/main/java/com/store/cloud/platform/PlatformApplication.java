package com.store.cloud.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lhd
 * @date 2020/12/22
 */
@SpringBootApplication(scanBasePackages = { "com.store.cloud" })
@EnableFeignClients(basePackages = {"com.store.cloud.api.**.feign"})
public class PlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformApplication.class, args);
	}

}

package com.store.cloud.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author FrozenWatermelon
 * @date 2020/9/10
 */
@SpringBootApplication(scanBasePackages = { "com.store.cloud" })
@EnableFeignClients(basePackages = {"com.store.cloud.api.**.feign"})
public class BizServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizServerApplication.class, args);
    }

}

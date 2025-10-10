package com.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableRetry
@EnableFeignClients(basePackages = "com.erp.FeignClient.Otp")
public class ERPApplication {
	public static void main(String[] args) {
		SpringApplication.run(ERPApplication.class, args);
	}
}
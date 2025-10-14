package com.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
@EnableRetry
public class ERPApplication {
	public static void main(String[] args) {
		SpringApplication.run(ERPApplication.class, args);
	}
}
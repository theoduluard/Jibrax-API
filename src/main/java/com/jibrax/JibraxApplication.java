package com.jibrax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "com.jibrax")
@PropertySource(value = "classpath:application-secret.properties", ignoreResourceNotFound = true)
public class JibraxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JibraxApplication.class, args);
	}
}
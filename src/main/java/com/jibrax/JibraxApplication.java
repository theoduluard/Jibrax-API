package com.jibrax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "com.jibrax")
@PropertySource("classpath:application-secret.properties")
public class JibraxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JibraxApplication.class, args);
	}
}
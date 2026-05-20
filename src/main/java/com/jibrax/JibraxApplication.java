package com.jibrax;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "com.jibrax")
@OpenAPIDefinition(
		servers = @Server(url = "https://jibrax-api.theoduluard.fr")
)
@PropertySource(value = "classpath:application-secret.properties", ignoreResourceNotFound = true)
public class JibraxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JibraxApplication.class, args);
	}
}
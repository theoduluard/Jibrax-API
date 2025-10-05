package com.jibrax;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Jibrax API",
                version = "1.0",
                description = "Documentation for Jibrax API, managing tasks, projects, users and teams."
        )
)
public class JibraxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JibraxApplication.class, args);
	}

}

package com.sirha.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ProyectoSirhaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoSirhaApplication.class, args);
	}

}

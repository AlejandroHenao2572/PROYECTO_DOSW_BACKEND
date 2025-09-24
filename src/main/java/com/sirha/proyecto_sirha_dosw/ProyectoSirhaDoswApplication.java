package com.sirha.proyecto_sirha_dosw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ProyectoSirhaDoswApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoSirhaDoswApplication.class, args);
	}

}

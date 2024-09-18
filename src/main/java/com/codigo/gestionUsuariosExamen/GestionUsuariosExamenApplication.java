package com.codigo.gestionUsuariosExamen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GestionUsuariosExamenApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionUsuariosExamenApplication.class, args);
	}

}

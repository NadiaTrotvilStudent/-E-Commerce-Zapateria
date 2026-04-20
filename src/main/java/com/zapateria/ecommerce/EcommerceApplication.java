package com.zapateria.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal de la aplicacion.
 * Spring Boot arranca desde esta clase y configura automaticamente el proyecto.
 */
@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		// Inicia el contenedor de Spring y levanta toda la aplicacion web.
		SpringApplication.run(EcommerceApplication.class, args);
	}

}

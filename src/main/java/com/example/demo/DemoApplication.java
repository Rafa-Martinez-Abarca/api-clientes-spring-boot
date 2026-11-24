package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Activa la configuración de Spring Boot y la búsqueda de componentes en este paquete
// (com.example.demo) y sus subpaquetes: controllers, servicios, etc.
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// Arranca el contexto de Spring y, con la dependencia web, el servidor integrado.
		// En este proyecto escucha en el puerto 8080 por defecto y sigue atendiendo peticiones.
		SpringApplication.run(DemoApplication.class, args);
	}

}

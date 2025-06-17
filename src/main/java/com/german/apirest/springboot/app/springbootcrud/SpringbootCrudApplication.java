package com.german.apirest.springboot.app.springbootcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal de la aplicación Spring Boot.
 * <p>
 * Anotada con {@link SpringBootApplication} para habilitar
 * la configuración automática, escaneo de componentes y más.
 * </p>
 *
 * @author German
 * @version 1.0
 */
@SpringBootApplication
public class SpringbootCrudApplication {

    /**
     * Método principal que lanza la aplicación.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringbootCrudApplication.class, args);
    }
}
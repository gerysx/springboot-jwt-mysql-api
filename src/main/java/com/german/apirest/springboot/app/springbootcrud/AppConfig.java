package com.german.apirest.springboot.app.springbootcrud;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Clase de configuración global de la aplicación.
 * <p>
 * Anotada con {@link Configuration} para declarar beans de configuración,
 * y con {@link PropertySource} para cargar el archivo de mensajes
 * <code>messages.properties</code> desde el classpath.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
@Configuration
@PropertySource("classpath:messages.properties")
public class AppConfig {
    // Configuración adicional de beans puede añadirse aquí
}
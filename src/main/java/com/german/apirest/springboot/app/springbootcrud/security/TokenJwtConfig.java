package com.german.apirest.springboot.app.springbootcrud.security;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;

/**
 * Contiene constantes de configuración para la gestión de JWT.
 * <p>
 * Define la clave secreta, prefijo de cabecera y nombre de cabeceras
 * usadas para autenticar peticiones.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
public class TokenJwtConfig {

    /**
     * Clave secreta utilizada para firmar y verificar tokens JWT.
     * <p>
     * Generada con algoritmo HS256 de JsonWebToken.<br>
     * Debe mantenerse segura y no exponerse en repositorios públicos.
     * </p>
     */
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    /**
     * Prefijo utilizado en la cabecera Authorization.
     * <p>Ejemplo: <code>Authorization: Bearer &lt;token&gt;</code>.</p>
     */
    public static final String PREFIX_TOKEN = "Bearer ";

    /**
     * Nombre de la cabecera HTTP donde se envía el token.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Tipo de contenido usado en las respuestas JSON de autenticación.
     */
    public static final String CONTENT_TYPE = "application/json";
}
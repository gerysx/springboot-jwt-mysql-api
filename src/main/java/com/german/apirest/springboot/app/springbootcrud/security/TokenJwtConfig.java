package com.german.apirest.springboot.app.springbootcrud.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {
    
     /**
     * Clave secreta para firmar el token JWT.
     * Se construye usando un algoritmo HS256.
     */
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public static final String PREFIX_TOKEN = "Bearer ";

    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String CONTENT_TYPE = "application/json";

    
}

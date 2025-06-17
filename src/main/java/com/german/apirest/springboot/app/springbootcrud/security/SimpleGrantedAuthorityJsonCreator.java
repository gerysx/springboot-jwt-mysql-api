package com.german.apirest.springboot.app.springbootcrud.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase abstracta para facilitar la deserialización de instancias de
 * {@code SimpleGrantedAuthority} desde JSON.
 * <p>
 * Proporciona un constructor anotado con {@link com.fasterxml.jackson.annotation.JsonCreator}
 * que recibe el campo <code>authority</code> y permite crear
 * objetos {@code GrantedAuthority} cuando se deserializa un token JWT.
 * </p>
 *
 * @param role nombre de la autoridad (p. ej. <code>ROLE_USER</code>, <code>ROLE_ADMIN</code>).
 */
public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(
        @JsonProperty("authority") String role) {
        // Constructor para uso de Jackson al deserializar GrantedAuthority
    }
}

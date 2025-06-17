package com.german.apirest.springboot.app.springbootcrud.services;

import java.util.List;
import com.german.apirest.springboot.app.springbootcrud.entities.User;

/**
 * Interfaz de servicio para operaciones básicas de usuario.
 * <p>
 * Define métodos para listar usuarios, guardar nuevos usuarios
 * y verificar existencia por nombre de usuario.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
public interface UserService {

    /**
     * Obtiene todos los usuarios del sistema.
     *
     * @return lista de todos los {@link User} registrados.
     */
    List<User> findAll();

    /**
     * Guarda un nuevo usuario, asignando roles y encriptando su contraseña.
     *
     * @param user entidad {@link User} a persistir.
     * @return usuario guardado con ID generado.
     */
    User save(User user);

    /**
     * Verifica si existe un usuario con el nombre dado.
     *
     * @param username nombre de usuario a comprobar.
     * @return {@code true} si ya existe, {@code false} en caso contrario.
     */
    boolean existsByUsername(String username);
}
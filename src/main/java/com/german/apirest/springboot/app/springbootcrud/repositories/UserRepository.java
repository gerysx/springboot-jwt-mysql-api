package com.german.apirest.springboot.app.springbootcrud.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.german.apirest.springboot.app.springbootcrud.entities.User;

/**
 * Repositorio CRUD para la entidad {@link User}.
 * <p>
 * Extiende {@link CrudRepository} para operaciones básicas
 * y define métodos para consulta por nombre de usuario
 * y validación de existencia.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Comprueba si existe un usuario con el nombre dado.
     *
     * @param username nombre de usuario único.
     * @return {@code true} si ya existe un usuario con ese nombre,
     *         {@code false} en caso contrario.
     */
    boolean existsByUsername(String username);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario único.
     * @return {@link Optional} con el User si se encuentra, o vacío si no existe.
     */
    Optional<User> findByUsername(String username);
}
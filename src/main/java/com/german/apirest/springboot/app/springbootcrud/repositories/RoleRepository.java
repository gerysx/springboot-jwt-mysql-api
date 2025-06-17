package com.german.apirest.springboot.app.springbootcrud.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.german.apirest.springboot.app.springbootcrud.entities.Role;

/**
 * Repositorio CRUD para la entidad {@link Role}.
 * <p>
 * Extiende {@link CrudRepository} para gestionar roles de seguridad
 * y añade método para buscar por nombre de rol.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Busca un rol por su nombre.
     *
     * @param name nombre único del rol (p.ej. ROLE_USER, ROLE_ADMIN).
     * @return {@link Optional} con el Role si existe, o vacío si no.
     */
    Optional<Role> findByName(String name);
}
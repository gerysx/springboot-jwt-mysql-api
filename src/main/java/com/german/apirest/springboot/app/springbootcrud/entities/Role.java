package com.german.apirest.springboot.app.springbootcrud.entities;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Entidad <code>Role</code> que representa un rol de seguridad asignable a usuarios.
 * <p>
 * Mapeada a la tabla <code>roles</code>, con relación many-to-many hacia {@link User}.
 * </p>
 *
 * @author German
 * @version 1.0
 */
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Identificador único del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre único del rol (p.ej. <code>ROLE_ADMIN</code>, <code>ROLE_USER</code>).
     */
    @Column(unique = true)
    private String name;

    /**
     * Lista de usuarios que tienen asignado este rol.
     * <p>
     * Ignorado en la serialización JSON para evitar bucles.
     * </p>
     */
    @JsonIgnoreProperties({"roles", "handler", "hibernateLazyInitializer"})
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    /**
     * Constructor por defecto que inicializa la lista de usuarios.
     */
    public Role() {
        this.users = new ArrayList<>();
    }

    /**
     * Crea un rol con nombre específico.
     *
     * @param name nombre del rol.
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Obtiene el ID del rol.
     *
     * @return identificador único.
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna el ID del rol.
     *
     * @param id identificador a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del rol.
     *
     * @return nombre único del rol.
     */
    public String getName() {
        return name;
    }

    /**
     * Asigna el nombre del rol.
     *
     * @param name nombre a establecer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la lista de usuarios asociados a este rol.
     *
     * @return lista de {@link User}.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Asigna la lista de usuarios para este rol.
     *
     * @param users colección de usuarios a asociar.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Calcula el hash code basado en <code>id</code> y <code>name</code>.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * Compara igualdad basándose en <code>id</code> y <code>name</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
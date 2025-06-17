package com.german.apirest.springboot.app.springbootcrud.entities;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.german.apirest.springboot.app.springbootcrud.validation.ExistsByUsername;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad <code>User</code> que representa un usuario del sistema.
 * <p>
 * Mapeada a la tabla <code>users</code>. Incluye validaciones de campo,
 * relaciones con roles y lógica de persistencia.
 * </p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único.
     * <p>
     * Validado para existencia previa en BD ({@link ExistsByUsername}),
     * no nulo y tamaño entre 4 y 12 caracteres.
     * </p>
     */
    @ExistsByUsername
    @Column(unique = true)
    @NotBlank
    @Size(min = 4, max = 12)
    private String username;

    /**
     * Contraseña del usuario.
     * <p>Se excluye de la serialización JSON de salida.
     * </p>
     */
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Roles asignados al usuario.
     * <p>
     * Relación many-to-many con tabla intermedia <code>users_roles</code>.
     * Evita ciclos en JSON con {@link JsonIgnoreProperties}.
     * </p>
     */
    @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})
    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","role_id"})
    )
    private List<Role> roles = new ArrayList<>();

    /**
     * Indica si el usuario está habilitado (activo).
     */
    private boolean enabled;

    /**
     * Indica si debe asignarse rol ADMIN al crear.
     * <p>
     * Transitorio y solo write-only en JSON.
     * </p>
     */
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    /**
     * Antes de persistir, habilita el usuario.
     */
    @PrePersist
    public void prePersist() {
        enabled = true;
    }

    /**
     * Constructor por defecto inicializa lista de roles.
     */
    public User() {
        this.roles = new ArrayList<>();
    }

    /**
     * Obtiene el ID del usuario.
     *
     * @return identificador único.
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna el ID del usuario.
     *
     * @param id identificador a asignar.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return nombre de usuario único.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asigna el nombre de usuario.
     *
     * @param username nombre único (4-12 caracteres).
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña (solo para uso interno).
     *
     * @return contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asigna la contraseña.
     *
     * @param password valor no nulo.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene la lista de roles asignados.
     *
     * @return lista de {@link Role} asociados.
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Asigna la lista de roles al usuario.
     *
     * @param roles colección de roles.
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Verifica si el usuario debe tener permisos de administrador.
     *
     * @return <code>true</code> si es admin, <code>false</code> en caso contrario.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Asigna el estado admin transitorio.
     *
     * @param admin indica si asignar rol ADMIN.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Comprueba si el usuario está habilitado.
     *
     * @return <code>true</code> si activo.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Asigna el estado de habilitación del usuario.
     *
     * @param enabled <code>true</code> para activar.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Calcula hash code basándose en <code>id</code> y <code>username</code>.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * Compara igualdad basándose en <code>id</code> y <code>username</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
}
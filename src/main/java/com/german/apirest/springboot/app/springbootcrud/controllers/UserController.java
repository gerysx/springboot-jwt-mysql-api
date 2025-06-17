package com.german.apirest.springboot.app.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.german.apirest.springboot.app.springbootcrud.entities.User;
import com.german.apirest.springboot.app.springbootcrud.services.UserService;

import jakarta.validation.Valid;

/**
 * REST controller for managing {@link User} entities.
 *
 * <p>
 * Exposes endpoints under <code>/api/users</code> for:
 * <ul>
 *   <li>Listing all users</li>
 *   <li>Creating new users (ADMIN only)</li>
 *   <li>Self-registration of non-admin users</li>
 * </ul>
 * Business logic is delegated to {@link UserService}.</p>
 *
 * @version 1.0
 * @since   1.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Servicio que gestiona la lógica de acceso a datos para usuarios.
     */
    @Autowired
    private UserService service;

    /**
     * Obtiene todos los usuarios existentes.
     *
     * <p>No requiere autenticación explícita; debe ser protegido
     * según configuración global de seguridad.</p>
     *
     * @return lista de todas las instancias de {@link User}.
     */
    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    /**
     * Crea un nuevo usuario con rol ADMIN.
     *
     * <p>Requiere rol <code>ADMIN</code> para acceder.</p>
     * <p>Valida el objeto {@link User} y, si hay errores,
     * devuelve <strong>400 Bad Request</strong> con detalles de validación.</p>
     *
     * @param user   objeto {@link User} a crear, deserializado del cuerpo de la petición.
     * @param result contenedor de errores de validación tras aplicar {@code @Valid}.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *           <li><strong>201 Created</strong> y el usuario guardado si la validación pasa.</li>
     *           <li><strong>400 Bad Request</strong> y mapa de mensajes de error si falla validación.</li>
     *         </ul>
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    /**
     * Registra un nuevo usuario con permisos de usuario estándar.
     *
     * <p>No requiere rol ADMIN; fuerza <code>admin=false</code>
     * y delega en {@link #create(User, BindingResult)}.</p>
     *
     * @param user   objeto {@link User} a registrar, validado con {@code @Valid}.
     * @param result contenedor de errores de validación.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *           <li><strong>201 Created</strong> y el usuario guardado si la validación pasa.</li>
     *           <li><strong>400 Bad Request</strong> y mapa de errores si falla validación.</li>
     *         </ul>
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
        user.setAdmin(false);
        return create(user, result);
    }

    /**
     * Construye la respuesta de error en caso de validación fallida.
     *
     * <p>Recorre los errores de campo en el {@link BindingResult} y
     * los acumula en un mapa donde la clave es el nombre del campo y
     * el valor es el mensaje de error.</p>
     *
     * @param result contenedor con los errores de validación.
     * @return {@link ResponseEntity} con estado <strong>400 Bad Request</strong>
     *         y cuerpo tipo {@code Map<String,String>} con los mensajes.
     */
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
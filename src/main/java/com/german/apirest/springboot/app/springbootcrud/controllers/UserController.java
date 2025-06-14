package com.german.apirest.springboot.app.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.german.apirest.springboot.app.springbootcrud.entities.User;
import com.german.apirest.springboot.app.springbootcrud.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Servicio que gestiona la lógica de acceso a datos para usuarios.
     * Inyectado automáticamente por Spring.
     */
    @Autowired
    private UserService service;

    /**
     * Obtiene todos los usuarios existentes.
     *
     * @return Lista de instancias de {@link User} recuperadas de la base de datos.
     */
    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    /**
     * Crea un nuevo usuario.
     *
     * <p>
     * Recibe un JSON que se deserializa en un objeto {@link User}, valida
     * sus campos según las anotaciones de Bean Validation y, si no hay errores,
     * lo persiste en la base de datos.
     * </p>
     *
     * @param user   Objeto {@link User} construido desde el cuerpo de la petición.
     *               Debe pasar las validaciones marcadas con {@code @Valid}.
     * @param result Contenedor de los posibles errores de validación.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *         <li>Estado <strong>201 Created</strong> y el usuario guardado en el
     *         cuerpo, si la validación es exitosa.</li>
     *         <li>Estado <strong>400 Bad Request</strong> y un mapa de mensajes de
     *         error, si hay fallos de validación.</li>
     *         </ul>
     */
    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {

        if (result.hasFieldErrors()) {
            return validation(result);
        }
        ;

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(user));
    }

    /**
     * POST /users/register
     *
     * Registra un nuevo usuario con permisos de usuario (no administrador).
     *
     * <p>
     * Recibe un JSON que se deserializa en un objeto {@link User}, valida sus
     * campos
     * según las anotaciones de Bean Validation y, si hay errores, devuelve un 400
     * Bad Request
     * con los detalles de validación.
     * </p>
     *
     * <p>
     * Si la validación es exitosa, fuerza el atributo {@code admin} a {@code false}
     * para asegurar que el usuario no tenga permisos de administrador, y luego
     * delega en el método {@link #create(User, BindingResult)} para persistir la
     * entidad
     * y construir la respuesta HTTP adecuada.
     * </p>
     *
     * @param user   Objeto {@link User} construido desde el cuerpo de la petición;
     *               debe pasar las validaciones marcadas con {@code @Valid}.
     * @param result Contenedor de los posibles errores de validación.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *         <li><strong>201 Created</strong> y el usuario guardado en el cuerpo,
     *         si la validación pasa.</li>
     *         <li><strong>400 Bad Request</strong> y un mapa de mensajes de error,
     *         si falla la validación.</li>
     *         </ul>
     */
    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
        
        user.setAdmin(false);
        return create(user, result);
    }

    /**
     * Construye la respuesta de error en caso de validación fallida.
     *
     * <p>
     * Recorre los errores de campo en el {@link BindingResult}, genera un mensaje
     * para cada campo inválido y los acumula en un mapa para devolverlo en JSON.
     * </p>
     *
     * @param result Objeto {@link BindingResult} que contiene los errores de
     *               validación.
     * @return {@link ResponseEntity} con estado <strong>400 Bad Request</strong> y
     *         cuerpo de tipo {@code Map<String,String>} donde la clave es el nombre
     *         del campo y el valor es el mensaje de error.
     */
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}

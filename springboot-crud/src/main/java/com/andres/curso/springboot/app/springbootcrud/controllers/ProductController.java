package com.andres.curso.springboot.app.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andres.curso.springboot.app.springbootcrud.entities.Product;
import com.andres.curso.springboot.app.springbootcrud.services.ProductService;

import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar operaciones CRUD sobre {@link Product}.
 *
 * <p><strong>Dependencias necesarias:</strong>
 * <ul>
 *   <li><code>spring-boot-starter-web</code> – para @RestController y manejo de rutas HTTP.</li>
 *   <li><code>spring-boot-starter-data-jpa</code> – para la persistencia de entidades Product.</li>
 *   <li><code>jakarta.validation-api</code> – para @Valid y validaciones de datos.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    /**
     * Servicio que implementa la lógica de negocio y acceso a datos para {@link Product}.
     * Inyectado automáticamente por Spring.
     */
    @Autowired
    private ProductService service;

    /**
     * Obtiene todos los productos disponibles.
     *
     * @return lista de {@link Product} con todos los productos persistidos.
     */
    @GetMapping
    public List<Product> list() {
        return service.findAll();
    }
    
    /**
     * Recupera un producto por su identificador.
     *
     * @param id Identificador único del producto a recuperar.
     * @return {@link ResponseEntity} con:
     * <ul>
     *   <li>200 OK y el producto en el cuerpo, si existe.</li>
     *   <li>404 Not Found, sin cuerpo, si no se encuentra.</li>
     * </ul>
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Crea un nuevo producto.
     *
     * <p>Valida el objeto {@link Product} recibido según sus anotaciones
     * de Bean Validation. Si hay errores, devuelve 400 con los detalles.</p>
     *
     * @param product  Objeto {@link Product} construido desde el cuerpo de la petición.
     * @param result   Contenedor de errores de validación tras aplicar {@code @Valid}.
     * @return {@link ResponseEntity} con:
     * <ul>
     *   <li>201 Created y el producto guardado en el cuerpo, si la validación pasa.</li>
     *   <li>400 Bad Request y un mapa de mensajes de error, si falla la validación.</li>
     * </ul>
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {
        // valdation.validate(product, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    /**
     * Actualiza un producto existente.
     *
     * <p>Valida el objeto {@link Product} recibido. Si no pasa validación,
     * responde 400 con detalles de error. Luego intenta actualizar el producto
     * con el {@code id} proporcionado.</p>
     *
     * @param product  Objeto {@link Product} con los datos a actualizar.
     * @param result   Contenedor de errores de validación tras aplicar {@code @Valid}.
     * @param id       Identificador del producto que se desea actualizar.
     * @return {@link ResponseEntity} con:
     * <ul>
     *   <li>201 Created y el producto actualizado, si existe y pasa validación.</li>
     *   <li>400 Bad Request y mapa de errores, si falla validación.</li>
     *   <li>404 Not Found, si el producto con ese {@code id} no existe.</li>
     * </ul>
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id) {
        // valdation.validate(product, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Optional<Product> productOptional = service.update(id, product);
        if (productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina un producto por su identificador.
     *
     * @param id Identificador del producto a eliminar.
     * @return {@link ResponseEntity} con:
     * <ul>
     *   <li>200 OK y el producto eliminado en el cuerpo, si existía.</li>
     *   <li>404 Not Found, si no se encuentra el producto.</li>
     * </ul>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> productOptional = service.delete(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Construye la respuesta de error cuando la validación de campos falla.
     *
     * <p>Recorre los errores de campo en el {@link BindingResult} y crea un mapa
     * donde la clave es el nombre del campo y el valor es un mensaje descriptivo.</p>
     *
     * @param result Contenedor con los errores de validación.
     * @return {@link ResponseEntity} con estado 400 Bad Request y
     *         cuerpo de tipo {@code Map<String,String>} con los mensajes de error.
     */
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}

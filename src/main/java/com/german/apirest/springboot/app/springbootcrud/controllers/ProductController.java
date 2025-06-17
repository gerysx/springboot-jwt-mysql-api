package com.german.apirest.springboot.app.springbootcrud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.german.apirest.springboot.app.springbootcrud.entities.Product;
import com.german.apirest.springboot.app.springbootcrud.services.ProductService;

import jakarta.validation.Valid;

/**
 * REST controller for managing CRUD operations on {@link Product} entities.
 *
 * <p>
 * Exposes endpoints under <code>/api/products</code> and secures access based on roles:
 * <ul>
 *   <li><strong>ADMIN</strong> – full access (create, update, delete).</li>
 *   <li><strong>USER</strong> – read-only access (list and view).</li>
 * </ul>
 * Delegates business logic to {@link ProductService}.</p>
 *
 * @version 1.0
 * @since   1.0
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * Servicio que implementa la lógica de negocio y acceso a datos para {@link Product}.
     */
    @Autowired
    private ProductService service;

    /**
     * Obtiene todos los productos disponibles.
     *
     * <p>Requiere rol <code>ADMIN</code> o <code>USER</code> para acceder.</p>
     *
     * @return lista de todos los {@link Product} persistidos.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Product> list() {
        return service.findAll();
    }
    
    /**
     * Recupera un producto por su identificador.
     *
     * <p>Requiere rol <code>ADMIN</code> o <code>USER</code> para acceder.</p>
     *
     * @param id identificador único del producto a recuperar.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *           <li><strong>200 OK</strong> y el producto en el cuerpo si existe.</li>
     *           <li><strong>404 Not Found</strong> si no se encuentra.</li>
     *         </ul>
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
     * <p>Requiere rol <code>ADMIN</code> para acceder.</p>
     * <p>Valida el objeto {@link Product} recibido y, si hay errores,
     * devuelve <strong>400 Bad Request</strong> con detalles.</p>
     *
     * @param product objeto {@link Product} a crear, deserializado del cuerpo de la petición.
     * @param result  contenedor de errores de validación tras aplicar {@code @Valid}.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *           <li><strong>201 Created</strong> y el producto guardado en el cuerpo si la validación pasa.</li>
     *           <li><strong>400 Bad Request</strong> y un mapa con mensajes de error si falla la validación.</li>
     *         </ul>
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    /**
     * Actualiza un producto existente.
     *
     * <p>Requiere rol <code>ADMIN</code> para acceder.</p>
     * <p>Valida el objeto {@link Product} y, si hay errores,
     * devuelve <strong>400 Bad Request</strong> con detalles. Si el producto no existe,
     * devuelve <strong>404 Not Found</strong>.</p>
     *
     * @param product objeto {@link Product} con datos para actualizar.
     * @param result  contenedor de errores de validación tras aplicar {@code @Valid}.
     * @param id      identificador del producto a actualizar.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *           <li><strong>201 Created</strong> y el producto actualizado si existe y pasa validación.</li>
     *           <li><strong>400 Bad Request</strong> y mapa de errores si falla validación.</li>
     *           <li><strong>404 Not Found</strong> si no existe producto con ese id.</li>
     *         </ul>
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id) {
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
     * <p>Requiere rol <code>ADMIN</code> para acceder.</p>
     *
     * @param id identificador del producto a eliminar.
     * @return {@link ResponseEntity} con:
     *         <ul>
     *           <li><strong>200 OK</strong> y el producto eliminado en el cuerpo si existía.</li>
     *           <li><strong>404 Not Found</strong> si no se encuentra el producto.</li>
     *         </ul>
     */
    @PreAuthorize("hasRole('ADMIN')")
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
     * donde la clave es el nombre del campo y el valor es el mensaje descriptivo.</p>
     *
     * @param result contenedor con los errores de validación.
     * @return {@link ResponseEntity} con estado <strong>400 Bad Request</strong> y
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
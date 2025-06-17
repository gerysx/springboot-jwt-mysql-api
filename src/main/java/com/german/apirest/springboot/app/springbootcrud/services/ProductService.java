package com.german.apirest.springboot.app.springbootcrud.services;

import java.util.List;
import java.util.Optional;
import com.german.apirest.springboot.app.springbootcrud.entities.Product;

/**
 * Interfaz de servicio para operaciones CRUD sobre {@link Product}.
 * <p>
 * Define métodos para listar, buscar por ID, crear, actualizar,
 * eliminar y verificar existencia por SKU.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
public interface ProductService {

    /**
     * Obtiene todos los productos.
     *
     * @return lista de todos los productos.
     */
    List<Product> findAll();

    /**
     * Busca un producto por su ID.
     *
     * @param id identificador del producto.
     * @return {@link Optional} con el producto si existe, o vacío si no.
     */
    Optional<Product> findById(Long id);

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param product entidad {@link Product} a guardar.
     * @return el producto persistido con su ID generado.
     */
    Product save(Product product);
    
    /**
     * Actualiza un producto existente.
     *
     * @param id      ID del producto a actualizar.
     * @param product datos del producto a actualizar.
     * @return {@link Optional} con el producto actualizado si existía, o vacío si no.
     */
    Optional<Product> update(Long id, Product product);

    /**
     * Elimina un producto por su ID.
     *
     * @param id identificador del producto a eliminar.
     * @return {@link Optional} con el producto eliminado si existía, o vacío si no.
     */
    Optional<Product> delete(Long id);

    /**
     * Verifica si existe un producto con el SKU dado.
     *
     * @param sku SKU único a verificar.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existsBySku(String sku);
}
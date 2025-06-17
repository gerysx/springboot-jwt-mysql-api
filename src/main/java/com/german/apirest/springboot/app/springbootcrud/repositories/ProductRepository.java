package com.german.apirest.springboot.app.springbootcrud.repositories;

import org.springframework.data.repository.CrudRepository;
import com.german.apirest.springboot.app.springbootcrud.entities.Product;

/**
 * Repositorio CRUD para la entidad {@link Product}.
 * <p>
 * Extiende {@link CrudRepository} para operaciones básicas de persistencia
 * y define métodos personalizados para la entidad Product.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

    /**
     * Comprueba si existe un producto con el SKU dado.
     *
     * @param sku SKU único del producto.
     * @return {@code true} si existe al menos un producto con ese SKU,
     *         {@code false} en caso contrario.
     */
    boolean existsBySku(String sku);
}
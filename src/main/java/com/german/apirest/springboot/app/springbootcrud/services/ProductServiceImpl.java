package com.german.apirest.springboot.app.springbootcrud.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.german.apirest.springboot.app.springbootcrud.entities.Product;
import com.german.apirest.springboot.app.springbootcrud.repositories.ProductRepository;

/**
 * Implementación de {@link ProductService} que utiliza JPA
 * a través de {@link ProductRepository}.
 * <p>
 * Gestiona transacciones para operaciones de lectura/escritura
 * sobre productos.</p>
 *
 * @version 1.0
 * @since   1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    /**
     * Repositorio JPA para persistencia de {@link Product}.
     */
    @Autowired
    private ProductRepository repository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Optional<Product> update(Long id, Product product) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            Product productDb = productOptional.orElseThrow();
            productDb.setSku(product.getSku());
            productDb.setName(product.getName());
            productDb.setDescription(product.getDescription());
            productDb.setPrice(product.getPrice());
            return Optional.of(repository.save(productDb));
        }
        return productOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Optional<Product> delete(Long id) {
        Optional<Product> productOptional = repository.findById(id);
        productOptional.ifPresent(repository::delete);
        return productOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }
}
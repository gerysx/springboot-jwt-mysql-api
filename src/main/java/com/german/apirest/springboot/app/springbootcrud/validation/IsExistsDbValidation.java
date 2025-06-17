package com.german.apirest.springboot.app.springbootcrud.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.german.apirest.springboot.app.springbootcrud.services.ProductService;

/**
 * Lógica de validación asociada a {@link IsExistsDb}.
 * <p>
 * Invoca {@link ProductService#existsBySku(String)} para verificar existencia.
 * Retorna {@code true} si el servicio no está disponible o
 * si el SKU no se encuentra en la base, {@code false} en caso contrario.
 * </p>
 *
 * @see IsExistsDb
 */
@Component
public class IsExistsDbValidation implements ConstraintValidator<IsExistsDb, String> {

    @Autowired
    private ProductService service;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (service == null) {
            return true;
        }
        return !service.existsBySku(value);
    }
}
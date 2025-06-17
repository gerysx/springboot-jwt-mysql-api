package com.german.apirest.springboot.app.springbootcrud.validation;

import org.springframework.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Lógica de validación asociada a {@link IsRequired}.
 * <p>
 * Comprueba que el valor no sea nulo, ni vacío, ni solo espacios en blanco.
 * </p>
 */
public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.hasText(value);
    }
}
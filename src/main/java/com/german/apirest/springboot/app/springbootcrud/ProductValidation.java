package com.german.apirest.springboot.app.springbootcrud;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.german.apirest.springboot.app.springbootcrud.entities.Product;

/**
 * Validador personalizado para la entidad {@link Product}.
 * <p>
 * Implementa {@link Validator} de Spring para comprobar campos
 * tales como nombre, descripción y precio, aplicando reglas
 * específicas de negocio.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Component
public class ProductValidation implements Validator {

    /**
     * Indica si la clase proporcionada es compatible con este validador.
     *
     * @param clazz clase del objeto a validar
     * @return {@code true} si puede validar objetos de tipo {@link Product}
     */
    @Override
    public boolean supports(@SuppressWarnings("null") Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    /**
     * Valida la instancia de {@link Product}, rechazando campos vacíos
     * o con valores fuera de rango.
     * <ul>
     * <li>Nombre: no vacío ni en blanco.</li>
     * <li>Descripción: no nula ni en blanco.</li>
     * <li>Precio: no nulo y >= 500.</li>
     * </ul>
     *
     * @param target objeto {@link Product} a validar
     * @param errors contenedor de errores para los campos inválidos
     */

    @SuppressWarnings("null")
    @Override
    public void validate(@SuppressWarnings("null") Object target, Errors errors) {
        Product product = (Product) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null, "es requerido!");
        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description",
        // "NotBlank.product.description");
        if (product.getDescription() == null || product.getDescription().isBlank()) {
            errors.rejectValue("description", null, "es requerido, por favor");
        }

        if (product.getPrice() == null) {
            errors.rejectValue("price", null, "no puede ser nulo, ok!");
        } else if (product.getPrice() < 500) {
            errors.rejectValue("price", null, "debe ser un valor numerico mayor o igual que 500!");
        }

    }

}

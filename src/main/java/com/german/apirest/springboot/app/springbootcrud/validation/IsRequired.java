package com.german.apirest.springboot.app.springbootcrud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validación personalizada para campos que deben ser requeridos
 * (no nulos, no vacíos, no solo espacios).
 * <p>
 * Se utiliza para comprobar texto mediante
 * {@link org.springframework.util.StringUtils#hasText(String)}.
 * </p>
 *
 * @see RequiredValidation
 */
@Constraint(validatedBy = RequiredValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface IsRequired {

    /**
     * Mensaje de error por defecto cuando el campo está vacío.
     * @return texto descriptivo
     */
    String message() default "es requerido usando anotaciones";

    /**
     * Define grupos de validación para esta restricción.
     * @return lista de grupos
     */
    Class<?>[] groups() default {};

    /**
     * Canales de carga útiles para mensajes de error.
     * @return lista de payloads
     */
    Class<? extends Payload>[] payload() default {};
}
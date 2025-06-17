package com.german.apirest.springboot.app.springbootcrud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validación personalizada para asegurar que un valor (e.g. SKU) no exista
 * ya en la base de datos.
 * <p>
 * Se delega en {@link IsExistsDbValidation} para la comprobación real.
 * </p>
 *
 * <p><strong>Ejemplo de uso:</strong>
 * <pre>
 *   @IsExistsDb(message = "Este SKU ya está registrado")
 *   private String sku;
 * </pre>
 * </p>
 *
 * @see IsExistsDbValidation
 */
@Constraint(validatedBy = IsExistsDbValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsExistsDb {

    /**
     * Mensaje de error cuando la validación falla.
     * @return texto descriptivo
     */
    String message() default "ya existe en la base de datos!";

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
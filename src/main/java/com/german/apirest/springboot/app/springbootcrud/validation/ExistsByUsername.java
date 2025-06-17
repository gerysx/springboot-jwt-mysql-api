package com.german.apirest.springboot.app.springbootcrud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validación personalizada para asegurar que un nombre de usuario
 * no exista previamente en la base de datos.
 * <p>
 * Aplica la lógica definida en {@link ExistsByUsernameValidation}.
 * Se usa sobre campos de tipo {@code String} que representan usernames.
 * </p>
 *
 * <p><strong>Ejemplo de uso:</strong>
 * <pre>
 *   @ExistsByUsername(message = "El nombre de usuario ya está en uso")
 *   private String username;
 * </pre>
 * </p>
 *
 * @see ExistsByUsernameValidation
 */
@Constraint(validatedBy = ExistsByUsernameValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByUsername {

    /**
     * Mensaje de error cuando la validación falla.
     * @return texto descriptivo
     */
    String message() default "ya existe en la BBDD || Escoja otro username";

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
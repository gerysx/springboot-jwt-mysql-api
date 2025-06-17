package com.german.apirest.springboot.app.springbootcrud.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.german.apirest.springboot.app.springbootcrud.services.UserService;

/**
 * Lógica de validación asociada a {@link ExistsByUsername}.
 * <p>
 * Invoca {@link UserService#existsByUsername(String)} para comprobar existencia.
 * Devuelve {@code true} si el servicio no está disponible (p.ej. en pruebas)
 * o si el nombre de usuario no existe aún, y {@code false} en caso contrario.
 * </p>
 *
 * @see ExistsByUsername
 */
@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

    @Autowired
    private UserService service;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // Si el servicio no se inyectó (p.ej. entorno de tests), se asume válido
        if (service == null) {
            return true;
        }
        // Valida que no exista ya el username
        return !service.existsByUsername(username);
    }
}
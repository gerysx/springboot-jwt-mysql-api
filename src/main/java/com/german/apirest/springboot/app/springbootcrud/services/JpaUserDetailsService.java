package com.german.apirest.springboot.app.springbootcrud.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.german.apirest.springboot.app.springbootcrud.entities.User;
import com.german.apirest.springboot.app.springbootcrud.repositories.UserRepository;

/**
 * Servicio que implementa {@link UserDetailsService} para cargar
 * los detalles de usuario desde la base de datos.
 * <p>
 * Utilizado por Spring Security durante la autenticación para obtener
 * el nombre de usuario, la contraseña y las autoridades de un usuario.
 * </p>
 *
 * @version 1.0
 * @since   1.0
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    /**
     * Repositorio JPA para acceder a los datos de {@link User}.
     */
    @Autowired
    private UserRepository repository;

    /**
     * Busca un usuario por nombre de usuario, lanza
     * {@link UsernameNotFoundException} si no existe, y convierte sus
     * roles en {@link GrantedAuthority} para el contexto de seguridad.
     *
     * @param username nombre de usuario a buscar.
     * @return {@link UserDetails} con credenciales y autoridades.
     * @throws UsernameNotFoundException si el usuario no está registrado.
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = repository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(
                String.format("Username %s no existe en el sistema!", username)
            );
        }
        User user = userOptional.orElseThrow();
        List<GrantedAuthority> authorities = user.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),    // cuenta habilitada
            true,                // cuenta no expirada
            true,                // credenciales no expiradas
            true,                // cuenta no bloqueada
            authorities          // autoridades (roles)
        );
    }
}
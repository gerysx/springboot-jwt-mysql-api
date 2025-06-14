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
 * la información de usuario desde la base de datos mediante JPA.
 * <p>
 * Se utiliza por Spring Security durante el proceso de autenticación
 * para obtener username, password y roles de un usuario.
 * </p>
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    /**
     * Repositorio JPA para acceder a la entidad {@link User}.
     */
    @Autowired
    private UserRepository repository;

    /**
     * Carga los detalles de un usuario dado su nombre de usuario.
     * <p>
     * Realiza una búsqueda en la base de datos; si no lo encuentra,
     * lanza {@link UsernameNotFoundException}. Si existe, convierte sus
     * roles en {@link GrantedAuthority} y devuelve un objeto {@link UserDetails}
     * que contiene username, password, estado de la cuenta y sus autoridades.
     * </p>
     *
     * @param username el nombre de usuario que se va a buscar
     * @return un objeto {@link UserDetails} con la información de autenticación
     * @throws UsernameNotFoundException si el usuario no existe en el sistema
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
            user.isEnabled(),    
            true,                
            true,                
            true,                
            authorities          
        );
    }
}

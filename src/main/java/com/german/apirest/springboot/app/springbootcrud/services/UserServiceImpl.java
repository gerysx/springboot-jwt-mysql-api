package com.german.apirest.springboot.app.springbootcrud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.german.apirest.springboot.app.springbootcrud.entities.Role;
import com.german.apirest.springboot.app.springbootcrud.entities.User;
import com.german.apirest.springboot.app.springbootcrud.repositories.RoleRepository;
import com.german.apirest.springboot.app.springbootcrud.repositories.UserRepository;

/**
 * Implementación de {@link UserService} que utiliza JPA
 * y encripta contraseñas con {@link PasswordEncoder}.
 * <p>
 * Asigna roles <code>ROLE_USER</code> y opcionalmente <code>ROLE_ADMIN</code>
     * según el campo transitorio <code>admin</code> del usuario.
     * </p>
     *
     * @version 1.0
     * @since   1.0
     */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Repositorio JPA para usuarios.
     */
    @Autowired
    private UserRepository repository;

    /**
     * Repositorio JPA para roles.
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Encoder para encriptar contraseñas.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User save(User user) {
        Optional<Role> optionalRolUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRolUser.ifPresent(roles::add);
        if (user.isAdmin()) {
            Optional<Role> optionalRolAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRolAdmin.ifPresent(roles::add);
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
}
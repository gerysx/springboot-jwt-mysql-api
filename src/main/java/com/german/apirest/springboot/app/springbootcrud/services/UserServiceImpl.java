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

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        Optional<Role> optionalRolUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        optionalRolUser.ifPresent(role -> roles.add(role));

        if(user.isAdmin()){
            Optional<Role> optionalRolAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRolAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        return repository.save(user);
    }
    
}

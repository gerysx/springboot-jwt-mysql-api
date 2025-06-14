package com.german.apirest.springboot.app.springbootcrud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.german.apirest.springboot.app.springbootcrud.entities.User;

public interface UserRepository extends CrudRepository<User, Long > {
    
}

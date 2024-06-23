package com.basic.basic_authorization_api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.basic.basic_authorization_api.models.Roles;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Integer> {

    Optional<Roles> findByName(String name);
    
}

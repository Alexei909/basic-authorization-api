package com.basic.basic_authorization_api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.basic.basic_authorization_api.models.Users;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
}

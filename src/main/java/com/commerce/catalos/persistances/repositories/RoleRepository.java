package com.commerce.catalos.persistances.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistances.dtos.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByUniqueId(String uniqueId);
}

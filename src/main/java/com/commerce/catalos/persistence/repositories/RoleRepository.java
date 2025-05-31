package com.commerce.catalos.persistence.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByUniqueId(String uniqueId);
}

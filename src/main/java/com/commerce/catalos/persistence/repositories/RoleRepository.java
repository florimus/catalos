package com.commerce.catalos.persistence.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Role;
import com.commerce.catalos.persistence.repositories.custom.RoleCustomRepository;

public interface RoleRepository extends MongoRepository<Role, String>, RoleCustomRepository {

    Optional<Role> findByUniqueId(final String uniqueId);

    Role findByUniqueIdAndEnabled(final String uniqueId, final boolean enabled);
}

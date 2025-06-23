package com.commerce.catalos.persistence.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.commerce.catalos.persistence.dtos.User;
import com.commerce.catalos.persistence.repositories.custom.UserCustomRepository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserCustomRepository {

    boolean existsByEmail(final String email);

    Optional<User> findByEmail(final String email);

    User findUserByIdAndEnabled(final String id, final boolean enabled);
}

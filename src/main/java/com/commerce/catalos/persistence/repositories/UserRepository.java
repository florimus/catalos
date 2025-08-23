package com.commerce.catalos.persistence.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.commerce.catalos.persistence.dtos.User;
import com.commerce.catalos.persistence.repositories.custom.UserCustomRepository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserCustomRepository {

    boolean existsByEmail(final String email);

    Optional<User> findByEmailAndEnabled(final String email, final Boolean enabled);

    User findUserByIdAndEnabled(final String id, final boolean enabled);

    User findUserByEmail(final String email);
}

package com.commerce.catalos.persistances.repositories;

import com.commerce.catalos.persistances.dtos.User;
import com.commerce.catalos.persistances.repositories.custom.UserCustomRepository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserCustomRepository {

    boolean existsByEmail(final String email);

    Optional<User> findByEmail(String email);
}

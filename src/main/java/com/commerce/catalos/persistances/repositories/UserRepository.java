package com.commerce.catalos.persistances.repositories;

import com.commerce.catalos.persistances.dtos.User;
import com.commerce.catalos.persistances.repositories.custom.UserCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserCustomRepository {

    boolean existsByEmail(final String email);
}

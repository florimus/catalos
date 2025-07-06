package com.commerce.catalos.persistence.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Address;

public interface AddressRepository extends MongoRepository<Address, String> {

    List<Address> findAllBySourceId(final String userId);

}

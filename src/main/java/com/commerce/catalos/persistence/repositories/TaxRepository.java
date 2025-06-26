package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Tax;

public interface TaxRepository extends MongoRepository<Tax, String> {

    Tax findTaxByIdAndEnabled(final String id, final boolean enabled);

}

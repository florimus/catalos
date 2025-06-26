package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Tax;
import com.commerce.catalos.persistence.repositories.custom.TaxCustomRepository;

public interface TaxRepository extends MongoRepository<Tax, String>, TaxCustomRepository {

    Tax findTaxByIdAndEnabled(final String id, final boolean enabled);

}

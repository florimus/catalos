package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Brand;
import com.commerce.catalos.persistence.repositories.custom.BrandCustomRepository;

public interface BrandRepository extends MongoRepository<Brand, String>, BrandCustomRepository {

    Brand findBrandByIdAndEnabled(final String id, final boolean enabled);

}

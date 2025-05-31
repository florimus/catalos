package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.ProductType;
import com.commerce.catalos.persistence.repositories.custom.ProductTypeCustomRepository;

public interface ProductTypeRepository extends MongoRepository<ProductType, String>, ProductTypeCustomRepository {

    boolean existsBySlug(final String slug);

    ProductType findProductTypeByIdAndEnabled(final String id, final boolean enabled);
}

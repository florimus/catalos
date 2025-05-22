package com.commerce.catalos.persistances.repositories;

import com.commerce.catalos.persistances.dtos.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductTypeRepository extends MongoRepository<ProductType, String> {
    boolean existsBySlug(final String slug);
}

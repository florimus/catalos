package com.commerce.catalos.persistances.repositories;

import com.commerce.catalos.persistances.dtos.ProductType;
import com.commerce.catalos.persistances.repositories.custom.ProductTypeCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductTypeRepository extends MongoRepository<ProductType, String>, ProductTypeCustomRepository {

    boolean existsBySlug(final String slug);

    ProductType findProductTypeByIdAndEnabled(final String id, final  boolean enabled);
}

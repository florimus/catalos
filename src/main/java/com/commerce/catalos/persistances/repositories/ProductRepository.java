package com.commerce.catalos.persistances.repositories;

import com.commerce.catalos.persistances.dtos.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

    boolean existsBySkuIdAndEnabled(final String skuId, final boolean enabled);
}

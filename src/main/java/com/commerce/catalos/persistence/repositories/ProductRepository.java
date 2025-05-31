package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Product;
import com.commerce.catalos.persistence.repositories.custom.ProductCustomRepository;

public interface ProductRepository extends MongoRepository<Product, String>, ProductCustomRepository {

    boolean existsBySkuIdAndEnabled(final String skuId, final boolean enabled);

    Product findProductByIdAndEnabled(final String id, final boolean enabled);
}

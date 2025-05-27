package com.commerce.catalos.persistances.repositories;

import com.commerce.catalos.persistances.dtos.Product;
import com.commerce.catalos.persistances.repositories.custom.ProductCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String>, ProductCustomRepository {

    boolean existsBySkuIdAndEnabled(final String skuId, final boolean enabled);

    Product findProductByIdAndEnabled(final String id, final boolean enabled);
}

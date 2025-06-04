package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, String> {

    Stock findByVariantIdAndEnabled(String variantId, boolean enabled);
}

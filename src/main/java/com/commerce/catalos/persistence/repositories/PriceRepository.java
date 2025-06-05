package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Price;

public interface PriceRepository extends MongoRepository<Price, String> {

    Price findBySkuIdAndEnabled(final String skuId, final boolean enabled);

}

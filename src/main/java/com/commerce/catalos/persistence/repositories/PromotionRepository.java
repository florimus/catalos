package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Discount;
import com.commerce.catalos.persistence.repositories.custom.PromotionCustomRepository;

public interface PromotionRepository extends MongoRepository<Discount, String>, PromotionCustomRepository {

    Discount findByIdAndEnabled(final String id, final boolean enabled);
}

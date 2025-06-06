package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Discount;

public interface PromotionRepository extends MongoRepository<Discount, String> {

}

package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Brand;

public interface BrandRepository extends MongoRepository<Brand, String> {

}

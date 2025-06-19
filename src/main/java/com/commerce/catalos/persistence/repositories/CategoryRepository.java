package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Category findCategoryByIdAndEnabled(final String id, final boolean enabled);

}

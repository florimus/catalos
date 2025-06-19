package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Category;
import com.commerce.catalos.persistence.repositories.custom.CategoryCustomRepository;

public interface CategoryRepository extends MongoRepository<Category, String>, CategoryCustomRepository {

    Category findCategoryByIdAndEnabled(final String id, final boolean enabled);

}

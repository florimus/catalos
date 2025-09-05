package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Category;
import com.commerce.catalos.persistence.repositories.custom.CategoryCustomRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String>, CategoryCustomRepository {

    Category findCategoryByIdAndEnabled(final String id, final boolean enabled);

    List<Category> findByIdInAndEnabled(final List<String> ids, final boolean enabled);
}

package com.commerce.catalos.persistence.repositories.custom;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Category;

public interface CategoryCustomRepository {

    Page<Category> searchCategories(final String search, final String parent, final Pageable pageable);
}

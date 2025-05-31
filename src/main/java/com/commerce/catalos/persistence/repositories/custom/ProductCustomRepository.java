package com.commerce.catalos.persistence.repositories.custom;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Product;

import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

    Page<Product> searchProducts(final String search, final Pageable pageable);
}

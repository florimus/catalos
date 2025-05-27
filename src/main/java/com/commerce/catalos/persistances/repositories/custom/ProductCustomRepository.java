package com.commerce.catalos.persistances.repositories.custom;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistances.dtos.Product;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

    Page<Product> searchProducts(final String search, final Pageable pageable);
}

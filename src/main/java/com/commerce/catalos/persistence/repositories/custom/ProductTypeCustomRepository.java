package com.commerce.catalos.persistence.repositories.custom;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.ProductType;

import org.springframework.data.domain.Pageable;

public interface ProductTypeCustomRepository {

    Page<ProductType> searchProductTypes(final String search, final Pageable pageable);
}

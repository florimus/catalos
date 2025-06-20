package com.commerce.catalos.persistence.repositories.custom;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Brand;

public interface BrandCustomRepository {

    Page<Brand> searchBrands(final String search, final Pageable pageable);
}

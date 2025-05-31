package com.commerce.catalos.persistence.repositories.custom;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Variant;

public interface VariantCustomRepository {

    Page<Variant> searchProductVariants(final String productId, final String search, final Pageable pageable);
}

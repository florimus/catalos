package com.commerce.catalos.persistence.repositories.custom;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Tax;

public interface TaxCustomRepository {

    Page<Tax> searchTaxes(final String search, final Pageable pageable);
}

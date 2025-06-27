package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.TaxResponse;
import com.commerce.catalos.models.taxes.TaxStatusUpdateResponse;
import com.commerce.catalos.models.taxes.UpdateTaxRequest;

public interface TaxService {

    TaxResponse createTax(final CreateTaxRequest createTaxRequest);

    TaxResponse updateTax(final String id, final UpdateTaxRequest updateTaxRequest);

    TaxResponse getTaxById(final String id);

    Page<TaxResponse> listTaxes(final String query, final String channels, final Pageable pageable);

    TaxStatusUpdateResponse updateTaxStatus(final String id, final boolean status);

}

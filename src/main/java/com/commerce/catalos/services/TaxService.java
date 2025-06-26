package com.commerce.catalos.services;

import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.CreateTaxResponse;
import com.commerce.catalos.models.taxes.UpdateTaxRequest;

public interface TaxService {

    CreateTaxResponse createTax(final CreateTaxRequest createTaxRequest);

    CreateTaxResponse updateTax(final String id, final UpdateTaxRequest updateTaxRequest);

    CreateTaxResponse getTaxById(final String id);

}

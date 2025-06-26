package com.commerce.catalos.services;

import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.CreateTaxResponse;

public interface TaxService {

    CreateTaxResponse createTax(final CreateTaxRequest createTaxRequest);

}

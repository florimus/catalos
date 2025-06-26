package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.CreateTaxResponse;
import com.commerce.catalos.persistence.dtos.Tax;

public class TaxHelper {

    public static Tax toTaxFromUpsertTaxRequest(final CreateTaxRequest createTaxRequest) {
        Tax tax = new Tax();
        BeanUtils.copyProperties(createTaxRequest, tax);
        return tax;
    }

    public static CreateTaxResponse toCreateTaxResponseFromTax(final Tax tax) {
        CreateTaxResponse createTaxResponse = new CreateTaxResponse();
        BeanUtils.copyProperties(tax, createTaxResponse);
        return createTaxResponse;
    }
}

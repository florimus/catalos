package com.commerce.catalos.helpers;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.TaxResponse;
import com.commerce.catalos.persistence.dtos.Tax;

public class TaxHelper {

    public static Tax toTaxFromUpsertTaxRequest(final CreateTaxRequest createTaxRequest) {
        Tax tax = new Tax();
        BeanUtils.copyProperties(createTaxRequest, tax);
        return tax;
    }

    public static TaxResponse toTaxResponseFromTax(final Tax tax) {
        TaxResponse taxResponse = new TaxResponse();
        BeanUtils.copyProperties(tax, taxResponse);
        return taxResponse;
    }

    public static List<TaxResponse> toTaxListResponseFromTaxes(final List<Tax> taxes) {
        return taxes.stream().map(TaxHelper::toTaxResponseFromTax).toList();
    }
}

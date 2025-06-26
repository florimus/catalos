package com.commerce.catalos.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.TaxHelper;
import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.CreateTaxResponse;
import com.commerce.catalos.models.taxes.UpdateTaxRequest;
import com.commerce.catalos.persistence.dtos.Tax;
import com.commerce.catalos.persistence.repositories.TaxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    private Tax findTaxById(final String id) {
        return taxRepository.findTaxByIdAndEnabled(id, true);
    }

    @Override
    public CreateTaxResponse createTax(final CreateTaxRequest createTaxRequest) {
        if (!createTaxRequest.isFixed() && createTaxRequest.getRate() > 100) {
            Logger.error("cf064b36-3d86-4e06-a9d1-ad00c8226e78", "Rate cannot be greater than 100");
            throw new BadRequestException("Rate cannot be greater than 100");
        }
        Tax tax = TaxHelper.toTaxFromUpsertTaxRequest(createTaxRequest);
        Logger.info("cb972480-a4ec-47b8-bd69-79b145135935", "creating tax class with name {}",
                tax.getName());
        tax = taxRepository.save(tax);
        return TaxHelper.toCreateTaxResponseFromTax(tax);
    }

    @Override
    public CreateTaxResponse updateTax(final String id, final UpdateTaxRequest updateTaxRequest) {
        Tax tax = findTaxById(id);
        if (null == tax) {
            Logger.error("7756637c-e74c-4aa2-89a6-be5efe2e4d02", "Tax not found with ID: {}", id);
            throw new NotFoundException("Tax not found");
        }
        BeanUtils.copyProperties(updateTaxRequest, tax);
        tax = taxRepository.save(tax);
        return TaxHelper.toCreateTaxResponseFromTax(tax);
    }

    @Override
    public CreateTaxResponse getTaxById(final String id) {
        Tax tax = findTaxById(id);
        if (null == tax) {
            Logger.error("7756637c-e74c-4aa2-89a6-be5efe2e4d02", "Tax not found with ID: {}", id);
            throw new NotFoundException("Tax not found");
        }
        return TaxHelper.toCreateTaxResponseFromTax(tax);
    }

}

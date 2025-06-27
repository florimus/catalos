package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.TaxHelper;
import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.TaxResponse;
import com.commerce.catalos.models.taxes.TaxStatusUpdateResponse;
import com.commerce.catalos.models.taxes.UpdateTaxRequest;
import com.commerce.catalos.persistence.dtos.Tax;
import com.commerce.catalos.persistence.repositories.TaxRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    private final AuthContext authContext;

    private Tax findTaxById(final String id) {
        return taxRepository.findTaxByIdAndEnabled(id, true);
    }

    @Override
    public TaxResponse createTax(final CreateTaxRequest createTaxRequest) {
        if (!createTaxRequest.isFixed() && createTaxRequest.getRate() > 100) {
            Logger.error("cf064b36-3d86-4e06-a9d1-ad00c8226e78", "Rate cannot be greater than 100");
            throw new BadRequestException("Rate cannot be greater than 100");
        }
        Tax tax = TaxHelper.toTaxFromUpsertTaxRequest(createTaxRequest);
        Logger.info("cb972480-a4ec-47b8-bd69-79b145135935", "creating tax class with name {}",
                tax.getName());
        tax = taxRepository.save(tax);
        return TaxHelper.toTaxResponseFromTax(tax);
    }

    @Override
    public TaxResponse updateTax(final String id, final UpdateTaxRequest updateTaxRequest) {
        Tax tax = findTaxById(id);
        if (null == tax) {
            Logger.error("abacb984-16a1-4290-b7b3-e4c97f361bc5", "Tax not found with ID: {}", id);
            throw new NotFoundException("Tax not found");
        }
        BeanUtils.copyProperties(updateTaxRequest, tax);
        tax = taxRepository.save(tax);
        return TaxHelper.toTaxResponseFromTax(tax);
    }

    @Override
    public TaxResponse getTaxById(final String id) {
        Tax tax = findTaxById(id);
        if (null == tax) {
            Logger.error("27580be7-69c7-43f9-abe2-fbc43ea04c0b", "Tax not found with ID: {}", id);
            throw new NotFoundException("Tax not found");
        }
        return TaxHelper.toTaxResponseFromTax(tax);
    }

    @Override
    public Page<TaxResponse> listTaxes(final String query, final String channels, final Pageable pageable) {
        Logger.info("e01793d1-a0a9-43a9-990c-9a6ad04068ef", "Finding taxes with query: {} and pageable: {}",
                query, pageable);
        Page<Tax> taxes = taxRepository.searchTaxes(query, channels, pageable);
        return new Page<TaxResponse>(
                TaxHelper.toTaxListResponseFromTaxes(taxes.getHits()),
                taxes.getTotalHitsCount(),
                taxes.getCurrentPage(),
                taxes.getPageSize());
    }

    @Override
    public TaxStatusUpdateResponse updateTaxStatus(String id, boolean status) {
        Tax tax = findTaxById(id);
        if (null == tax) {
            Logger.error("30dedae3-b40a-481c-88ff-51ece72da250", "Tax not found with ID: {}", id);
            throw new NotFoundException("Tax not found");
        }
        tax.setActive(status);
        tax.setUpdatedAt(new Date());
        tax.setUpdatedBy(authContext.getCurrentUser().getEmail());
        tax = taxRepository.save(tax);
        return TaxStatusUpdateResponse.builder().message(tax.isActive() ? "Tax Activated" : "Tax Deactivated")
                .status(tax.isActive()).build();
    }

}

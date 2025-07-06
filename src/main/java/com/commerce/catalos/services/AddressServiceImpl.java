package com.commerce.catalos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.helpers.AddressHelper;
import com.commerce.catalos.models.address.AddressResponse;
import com.commerce.catalos.models.address.CreateAddressRequest;
import com.commerce.catalos.persistence.dtos.Address;
import com.commerce.catalos.persistence.repositories.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public AddressResponse createAddress(final CreateAddressRequest createAddressRequest) {
        Logger.info("250e11e2-c568-4850-95de-9c8737598f96", "Creating new address : {}",
                createAddressRequest.getSourceId());
        Address address = addressRepository.save(AddressHelper.toAddress(createAddressRequest));
        return AddressHelper.toAddressResponse(address);
    }

    @Override
    public List<AddressResponse> getAddresses(final String userId) {
        if (null == userId || userId.isBlank()) {
            Logger.error("0d0bea8f-5563-43b1-b4f3-d80647d91dec", "User id is mandatory");
            throw new BadRequestException("User id is mandatory");
        }
        List<Address> addresses = addressRepository.findAllBySourceId(userId);
        return AddressHelper.toAddressResponseFromAddresses(addresses);
    }

}

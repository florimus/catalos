package com.commerce.catalos.services;

import java.util.List;

import com.commerce.catalos.models.address.AddressResponse;
import com.commerce.catalos.models.address.CreateAddressRequest;

public interface AddressService {

    AddressResponse createAddress(final CreateAddressRequest createAddressRequest);

    List<AddressResponse> getAddresses(final String userId);

}

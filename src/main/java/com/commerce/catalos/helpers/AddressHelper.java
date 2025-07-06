package com.commerce.catalos.helpers;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.address.AddressResponse;
import com.commerce.catalos.models.address.CreateAddressRequest;
import com.commerce.catalos.persistence.dtos.Address;

public class AddressHelper {

    public static Address toAddress(final CreateAddressRequest createAddressRequest) {
        Address address = new Address();
        BeanUtils.copyProperties(createAddressRequest, address);
        return address;
    }

    public static AddressResponse toAddressResponse(final Address address) {
        AddressResponse addressResponse = new AddressResponse();
        BeanUtils.copyProperties(address, addressResponse);
        return addressResponse;
    }

    public static List<AddressResponse> toAddressResponseFromAddresses(List<Address> addresses) {
        return addresses.stream().map(AddressHelper::toAddressResponse).toList();
    }
}

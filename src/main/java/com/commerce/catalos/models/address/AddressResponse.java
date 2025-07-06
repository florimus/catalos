package com.commerce.catalos.models.address;

import com.commerce.catalos.core.enums.AddressType;

import lombok.Data;

@Data
public class AddressResponse {

    private String sourceId;

    private AddressType addressType;

    private String phone;

    private String addressLine1;

    private String addressLine2;

    private String country;

    private String state;

    private String city;

    private String area;

    private Integer pinCode;
}

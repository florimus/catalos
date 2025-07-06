package com.commerce.catalos.controllers;

import java.util.List;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.address.AddressResponse;
import com.commerce.catalos.models.address.CreateAddressRequest;
import com.commerce.catalos.services.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressControllerV1 {

    private final AddressService addressService;

    @PostMapping()
    public ResponseEntity<AddressResponse> createAddress(
            @RequestBody final @Valid CreateAddressRequest createAddressRequest) {
        Logger.info("15a870fe-2418-4c68-8bd2-82342a7798cf",
                "Received request for creating new address : {}", createAddressRequest.getSourceId());
        return new ResponseEntity<AddressResponse>(
                addressService.createAddress(createAddressRequest));
    }

    @GetMapping("/customer/id/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddresses(@PathVariable final String userId) {
        Logger.info("0d0bea8f-5563-43b1-b4f3-d80647d91dec",
                "Received request for getting address of user: {}", userId);
        return new ResponseEntity<List<AddressResponse>>(
                addressService.getAddresses(userId));
    }
}

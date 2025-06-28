package com.commerce.catalos.controllers;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody final @Valid CreateOrderRequest createOrderRequest) {
        Logger.info("e664912c-482a-4afc-bf33-25f5b844cf1c",
                "Received request for creating order in channel: {}", createOrderRequest.getChannelId());
        return new ResponseEntity<OrderResponse>(orderService.createOrder(createOrderRequest));
    }
}

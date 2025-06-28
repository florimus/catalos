package com.commerce.catalos.controllers;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.DeleteOrderLineItemRequest;
import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.models.orders.UpdateOrderLineItemRequest;
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
                "Received request for creating order in channel: {}",
                createOrderRequest.getChannelId());
        return new ResponseEntity<OrderResponse>(orderService.createOrder(createOrderRequest));
    }

    @PutMapping("/id/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderLineItems(
            @PathVariable final String orderId,
            @RequestBody final @Valid UpdateOrderLineItemRequest updateOrderLineItemRequest) {
        Logger.info("bd39b927-6957-4610-beb2-ec8d33cc099d",
                "Received request for updating line items in order: {}", orderId);
        return new ResponseEntity<OrderResponse>(
                orderService.updateOrderLineItems(orderId, updateOrderLineItemRequest));
    }

    @DeleteMapping("/id/{orderId}/line-items")
    public ResponseEntity<OrderResponse> deleteOrderLineItems(
            @PathVariable final String orderId,
            @RequestBody final @Valid DeleteOrderLineItemRequest deleteOrderLineItemRequest) {
        Logger.info("9e18b623-7709-4040-a0b8-3e694e89af69",
                "Received request for delete line items in order: {}", orderId);
        return new ResponseEntity<OrderResponse>(
                orderService.deleteOrderLineItems(orderId, deleteOrderLineItemRequest));
    }
}

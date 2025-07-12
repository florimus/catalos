package com.commerce.catalos.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.DeleteOrderLineItemRequest;
import com.commerce.catalos.models.orders.MiniOrderResponse;
import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.models.orders.UpdateOrderLineItemRequest;
import com.commerce.catalos.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    @PreAuthorize("hasRole('ORD:NN')")
    public ResponseEntity<OrderResponse> createOrderByAdmin(
            @RequestBody final @Valid CreateOrderRequest createOrderRequest) {
        Logger.info("4ce01fb4-8772-4a5a-a082-1824f4a9d475",
                "Received request for creating order in channel: {}",
                createOrderRequest.getChannelId());
        return new ResponseEntity<OrderResponse>(orderService.createOrderByAdmin(createOrderRequest));
    }

    @GetMapping("/id/{orderId}")
    @PreAuthorize("hasRole('ORD:LS')")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable("orderId") final String orderId) {
        Logger.info("3e2b6cd3-d782-4333-bc21-e6fbb95a7655",
                "Received request for fetch order: {}", orderId);
        return new ResponseEntity<OrderResponse>(orderService.getOrderById(orderId));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ORD:LS')")
    public ResponseEntity<Page<MiniOrderResponse>> getOrders(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "") String channel,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Direction.DESC) Pageable pageable) {
        Logger.info("598f3922-5e91-4571-a31b-48fc03b75178",
                "Received request for search order: {}", query);
        return new ResponseEntity<Page<MiniOrderResponse>>(orderService.getOrders(query, channel, pageable));
    }

    @PutMapping("/id/{orderId}")
    @PreAuthorize("hasRole('ORD:NN')")
    public ResponseEntity<OrderResponse> updateOrderLineItems(
            @PathVariable("orderId") final String orderId,
            @RequestBody final @Valid UpdateOrderLineItemRequest updateOrderLineItemRequest) {
        Logger.info("a1bb0ea4-bf7d-497f-87ba-cab908cb14fe",
                "Received request for updating line items in order: {}", orderId);
        return new ResponseEntity<OrderResponse>(
                orderService.updateOrderLineItems(orderId, updateOrderLineItemRequest));
    }

    @PreAuthorize("hasRole('ORD:NN')")
    @DeleteMapping("/id/{orderId}/line-items")
    public ResponseEntity<OrderResponse> deleteOrderLineItems(
            @PathVariable("orderId") final String orderId,
            @RequestBody final @Valid DeleteOrderLineItemRequest deleteOrderLineItemRequest) {
        Logger.info("1c4da7fc-94cb-410b-8986-9b35e2e00613",
                "Received request for delete line items in order: {}", orderId);
        return new ResponseEntity<OrderResponse>(
                orderService.deleteOrderLineItems(orderId, deleteOrderLineItemRequest));
    }
}

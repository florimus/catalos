package com.commerce.catalos.controllers;

import com.commerce.catalos.models.orders.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
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

    @PutMapping("/search")
    @PreAuthorize("hasRole('ORD:LS')")
    public ResponseEntity<Page<MiniOrderResponse>> getOrders(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String channel,
            @RequestBody(required = false) final OrderFilterInputs orderFilterInputs,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Direction.DESC) Pageable pageable) {
        Logger.info("598f3922-5e91-4571-a31b-48fc03b75178",
                "Received request for search order: {}", query);
        return new ResponseEntity<Page<MiniOrderResponse>>(
                orderService.getOrders(query, channel, orderFilterInputs, pageable));
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

    @PreAuthorize("hasRole('ORD:NN')")
    @PatchMapping("/id/{orderId}/option/{optionId}")
    public ResponseEntity<OrderResponse> selectPaymentMethod(
            @PathVariable("orderId") final String orderId,
            @PathVariable("optionId") final String optionId) {
        Logger.info("06a02dba-91f0-4883-9798-4c03ff426415",
                "Received request for select payment option: {} for order: {}",
                optionId, orderId);
        return new ResponseEntity<OrderResponse>(orderService.selectPaymentMethod(orderId, optionId));
    }

    @PreAuthorize("hasRole('ORD:NN')")
    @PatchMapping("/id/{orderId}/submit")
    public ResponseEntity<OrderResponse> submitOrder(
            @PathVariable("orderId") final String orderId) {
        Logger.info("33747581-7ece-4430-aadd-27c1b4cf2d72",
                "Received request for submit order: {}", orderId);
        return new ResponseEntity<OrderResponse>(orderService.submitOrder(orderId));
    }

    @PreAuthorize("hasRole('ORD:NN')")
    @PutMapping("/id/{orderId}/transaction")
    public ResponseEntity<OrderResponse> updateOrderTransaction(
            @PathVariable("orderId") final String orderId,
            @RequestBody final @Valid OrderTransactionRequest orderTransactionRequest) {
        Logger.info("df6d91fa-12a3-45f0-9057-590af992996f",
                "Received request for update order payment transaction: {} for order: {}",
                orderTransactionRequest.getPaymentUniqueId(), orderId);
        return new ResponseEntity<OrderResponse>(
                orderService.updateOrderTransaction(orderId, orderTransactionRequest));
    }

    @PatchMapping("/id/{orderId}/link")
    @PreAuthorize("hasRole('ORD:NN')")
    public ResponseEntity<OrderLinkResponse> getPaymentLinkOfOrderById(
            @PathVariable("orderId") final String orderId) {
        Logger.info("c9c69f2e-2abb-48c4-82d4-16a69af11087",
                "Received request for fetch payment link of order: {}", orderId);
        return new ResponseEntity<OrderLinkResponse>(orderService.getPaymentLinkOfOrderById(orderId));
    }

    @PreAuthorize("hasRole('ORD:NN')")
    @PatchMapping("/id/{orderId}/packaging")
    public ResponseEntity<OrderResponse> updateOrderPackaging(
            @PathVariable("orderId") final String orderId,
            @RequestBody final OrderPackagingInfoRequest orderPackagingInfoRequest) {
        Logger.info("a4cbe0be-f187-4075-90e1-771f54397d8b",
                "Received request for update order packaging requests: {} for order: {}",
                orderPackagingInfoRequest, orderId);
        return new ResponseEntity<OrderResponse>(
                orderService.updateOrderPackaging(orderId, orderPackagingInfoRequest));
    }
}

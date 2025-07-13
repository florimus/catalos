package com.commerce.catalos.services;

import com.commerce.catalos.models.orders.*;
import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;

public interface OrderService {

    OrderResponse createOrder(final CreateOrderRequest createOrderRequest);

    OrderResponse updateOrderLineItems(final String orderId,
            final UpdateOrderLineItemRequest updateOrderLineItemRequest);

    OrderResponse deleteOrderLineItems(final String orderId,
            final DeleteOrderLineItemRequest deleteOrderLineItemRequest);

    OrderResponse getOrderById(final String orderId);

    OrderResponse createOrderByAdmin(final CreateOrderRequest createOrderRequest);

    Page<MiniOrderResponse> getOrders(final String query, final String channel, final Pageable pageable);

    OrderResponse updateAddress(final String orderId, final UpdateAddressRequest updateAddressRequest);

    OrderResponse selectPaymentMethod(final String orderId, final String optionId);

    OrderResponse updateOrderTransaction(final String orderId,
            final OrderTransactionRequest orderTransactionRequest);
}

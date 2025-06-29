package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.DeleteOrderLineItemRequest;
import com.commerce.catalos.models.orders.MiniOrderResponse;
import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.models.orders.UpdateOrderLineItemRequest;

public interface OrderService {

    OrderResponse createOrder(final CreateOrderRequest createOrderRequest);

    OrderResponse updateOrderLineItems(final String orderId,
            final UpdateOrderLineItemRequest updateOrderLineItemRequest);

    OrderResponse deleteOrderLineItems(final String orderId,
            final DeleteOrderLineItemRequest deleteOrderLineItemRequest);

    OrderResponse getOrderById(final String orderId);

    OrderResponse createOrderByAdmin(final CreateOrderRequest createOrderRequest);

    Page<MiniOrderResponse> getOrders(final String query, final String channel, final Pageable pageable);

}

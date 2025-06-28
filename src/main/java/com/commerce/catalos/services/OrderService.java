package com.commerce.catalos.services;

import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.DeleteOrderLineItemRequest;
import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.models.orders.UpdateOrderLineItemRequest;

public interface OrderService {

    OrderResponse createOrder(final CreateOrderRequest createOrderRequest);

    OrderResponse updateOrderLineItems(final String orderId,
            final UpdateOrderLineItemRequest updateOrderLineItemRequest);

    OrderResponse deleteOrderLineItems(final String orderId,
            final DeleteOrderLineItemRequest deleteOrderLineItemRequest);

}

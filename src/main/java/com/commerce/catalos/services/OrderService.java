package com.commerce.catalos.services;

import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(final CreateOrderRequest createOrderRequest);

}

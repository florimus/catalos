package com.commerce.catalos.resolvers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.services.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartResolvers {

    private final OrderService orderService;

    @QueryMapping
    public OrderResponse getCart(@Argument("id") final String id) {
        return orderService.getOrderById(id);
    }
}

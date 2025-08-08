package com.commerce.catalos.resolvers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.OrderRequestLineItem;
import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.security.RequestContext;
import com.commerce.catalos.services.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartResolvers {

    private final RequestContext requestContext;

    private final OrderService orderService;

    @QueryMapping
    public OrderResponse getCart(@Argument("id") final String id) {
        return orderService.getOrderById(id);
    }

    @MutationMapping
    public OrderResponse createCart(@Argument("lineItems") final List<OrderRequestLineItem> lineItems) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setChannelId(requestContext.getChannel());
        createOrderRequest.setLineItems(lineItems);
        return orderService.createOrder(createOrderRequest);
    }

    @MutationMapping
    public OrderResponse updateCartItem(
            @Argument("id") final String id,
            @Argument("updateItems") final UpdateOrderLineItemRequest updateOrderLineItemRequest) {
        return orderService.updateOrderLineItems(id, updateOrderLineItemRequest);
    }
}

package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.core.enums.OrderStatus;
import com.commerce.catalos.persistence.dtos.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

    Order findOrderByUserIdAndChannelIdAndStatusAndActiveAndEnabled(final String userId, final String channelId,
            final OrderStatus inProgress, final boolean active, final boolean enabled);

    Order findOrderByIdAndEnabled(final String orderId, final boolean enabled);

}

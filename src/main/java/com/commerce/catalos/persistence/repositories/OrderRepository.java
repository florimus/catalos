package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.core.enums.OrderStatus;
import com.commerce.catalos.persistence.dtos.Order;
import com.commerce.catalos.persistence.repositories.custom.OrderCustomRepository;

public interface OrderRepository extends MongoRepository<Order, String>, OrderCustomRepository {

    Order findOrderByUserIdAndChannelIdAndStatusAndActiveAndEnabled(final String userId, final String channelId,
            final OrderStatus status, final boolean active, final boolean enabled);

    Order findOrderByIdAndEnabled(final String orderId, final boolean enabled);

    Order findOrderByEmailAndChannelIdAndStatusAndActiveAndEnabled(final String email, final String channelId,
            final OrderStatus status, boolean active, boolean enabled);

    Order findOrderByIdAndStatusAndEnabled(final String orderId, final OrderStatus orderStatus, final boolean enabled);
}

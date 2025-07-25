package com.commerce.catalos.persistence.repositories.custom;

import com.commerce.catalos.models.orders.OrderFilterInputs;
import org.springframework.data.domain.Pageable;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Order;

public interface OrderCustomRepository {

    public Page<Order> searchOrders(
            final String query, final String channel, final OrderFilterInputs orderFilterInputs, final Pageable pageable);
}

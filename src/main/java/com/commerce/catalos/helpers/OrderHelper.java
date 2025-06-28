package com.commerce.catalos.helpers;

import com.commerce.catalos.models.orders.LineItemPrice;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.persistence.dtos.Order;

public class OrderHelper {

    public static OrderResponse toOrderResponseFromOrder(final Order order) {
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(order, orderResponse);
        return orderResponse;
    }

    public static LineItemPrice toLineItemPriceFromCalculatedPriceResponse (
            final CalculatedPriceResponse calculatedPriceResponse) {
        LineItemPrice lineItemPrice = new LineItemPrice();
        BeanUtils.copyProperties(calculatedPriceResponse, lineItemPrice);
        return lineItemPrice;
    }
}

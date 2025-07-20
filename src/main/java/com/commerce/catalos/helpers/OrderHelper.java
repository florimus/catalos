package com.commerce.catalos.helpers;

import com.commerce.catalos.models.orders.*;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.variants.MiniVariantResponse;
import com.commerce.catalos.models.variants.VariantResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.persistence.dtos.Address;
import com.commerce.catalos.persistence.dtos.Order;

public class OrderHelper {

    public static OrderResponse toOrderResponseFromOrder(final Order order) {
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(order, orderResponse);
        return orderResponse;
    }

    public static LineItemPrice toLineItemPriceFromCalculatedPriceResponse(
            final CalculatedPriceResponse calculatedPriceResponse) {
        LineItemPrice lineItemPrice = new LineItemPrice();
        BeanUtils.copyProperties(calculatedPriceResponse, lineItemPrice);
        return lineItemPrice;
    }

    public static MiniOrderResponse toMiniOrderResponseFromOrder(final Order order) {
        MiniOrderResponse miniOrderResponse = new MiniOrderResponse();
        BeanUtils.copyProperties(order, miniOrderResponse);
        List<MiniLineItem> miniLineItems = null;
        if (order.getLineItems() != null) {
            miniLineItems = order.getLineItems().stream().map(item -> {
                MiniLineItem miniLineItem = new MiniLineItem();
                BeanUtils.copyProperties(item, miniLineItem);
                String productName = item.getProduct() != null ? item.getProduct().getName() : null;

                miniLineItem.setProductName(productName);

                VariantResponse variant = item.getVariant();
                MiniVariantResponse miniVariant = new MiniVariantResponse();
                if (variant != null) {
                    BeanUtils.copyProperties(variant, miniVariant);
                }

                miniLineItem.setVariant(miniVariant);

                return miniLineItem;
            }).toList();
        }

        miniOrderResponse.setLineItems(miniLineItems);

        return miniOrderResponse;
    }

    public static List<MiniOrderResponse> toMiniOrderResponseFromOrders(final List<Order> orders) {
        return orders.stream().map(OrderHelper::toMiniOrderResponseFromOrder).toList();
    }

    public static Address toAddressFromAddressResponse(final UpdateAddressRequest updateAddressRequest) {
        Address address = new Address();
        BeanUtils.copyProperties(updateAddressRequest, address);
        return address;
    }

    public static Map<String, EventItem> createOrderEvent(final String event, final String user) {
        return Map.of(event, new EventItem(new Date(), user, null));
    }

    public static Map<String, EventItem> createOrderEvent(final String event, final String user, final String note) {
        return Map.of(event, new EventItem(new Date(), user, note));
    }


    public static Map<String, EventItem> updateOrderEvent(Map<String, EventItem> events, final String event, final String user) {
        if (null == events){
            return Map.of(event, new EventItem(new Date(), user, null));
        }
        events.put(event, new EventItem(new Date(), user, null));
        return events;
    }

    public static Map<String, EventItem> updateOrderEvent(Map<String, EventItem> events, final String event, final String user, final String note) {
        if (null == events){
            return Map.of(event, new EventItem(new Date(), user, note));
        }
        events.put(event, new EventItem(new Date(), user, note));
        return events;
    }

}

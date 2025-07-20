package com.commerce.catalos.models.orders;

import java.util.List;
import java.util.Map;

import com.commerce.catalos.core.enums.OrderStatus;
import com.commerce.catalos.persistence.dtos.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private String id;

    private OrderStatus status;

    private String userId;

    private String email;

    private boolean isGuestOrder;

    private String channelId;

    private List<LineItem> lineItems;

    private String coupon;

    private OrderPrice price;

    private Address shippingAddress;

    private Address billingAddress;

    private PaymentInfo paymentInfo;

    private Map<String, EventItem> events;

    private boolean active;
}

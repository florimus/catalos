package com.commerce.catalos.models.orders;

import java.util.List;

import com.commerce.catalos.core.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniOrderResponse {

    private String id;

    private OrderStatus status;

    private String userId;

    private String email;

    private boolean isGuestOrder;

    private String channelId;

    private List<MiniLineItem> lineItems;

    private String coupon;

    private OrderPrice price;
}

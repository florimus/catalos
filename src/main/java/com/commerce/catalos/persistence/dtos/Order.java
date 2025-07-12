package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.core.enums.OrderStatus;
import com.commerce.catalos.models.orders.LineItem;
import com.commerce.catalos.models.orders.OrderPrice;
import com.commerce.catalos.models.orders.PaymentInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("cat_orders")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseDto {

    @Id
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

    private List<PaymentOption> paymentOptions;

    private PaymentInfo paymentInfo;
}

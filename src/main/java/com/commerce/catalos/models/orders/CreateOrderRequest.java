package com.commerce.catalos.models.orders;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private String userId;

    @NotBlank(message = "channelId is mandatory")
    private String channelId;

    private List<OrderRequestLineItem> lineItems;
}

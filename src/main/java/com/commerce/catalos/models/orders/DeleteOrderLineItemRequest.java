package com.commerce.catalos.models.orders;

import java.util.List;

import lombok.Data;

@Data
public class DeleteOrderLineItemRequest {

    private List<String> lineItems;
}

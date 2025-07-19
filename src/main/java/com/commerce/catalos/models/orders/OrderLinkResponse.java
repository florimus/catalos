package com.commerce.catalos.models.orders;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLinkResponse {

    private String paymentLink;
}

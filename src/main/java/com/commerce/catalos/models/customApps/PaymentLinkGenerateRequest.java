package com.commerce.catalos.models.customApps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkGenerateRequest {

    private Float amount;

    private String name;

    private String currency;

    private String email;

    private String contact;

    private String orderId;
}

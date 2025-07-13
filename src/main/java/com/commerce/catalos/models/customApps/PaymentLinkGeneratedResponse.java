package com.commerce.catalos.models.customApps;

import lombok.Data;

@Data
public class PaymentLinkGeneratedResponse {

    private String id;

    private String paymentUrl;

    private String reference_id;
}

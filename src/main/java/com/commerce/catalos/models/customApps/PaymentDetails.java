package com.commerce.catalos.models.customApps;

import lombok.Data;

@Data
public class PaymentDetails {

    private String payment_id;

    private String method;

    private String created_at;
}

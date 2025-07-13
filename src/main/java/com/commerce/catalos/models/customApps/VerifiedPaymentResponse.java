package com.commerce.catalos.models.customApps;

import lombok.Data;

@Data
public class VerifiedPaymentResponse {

    private boolean success;

    private String message;

    private PaymentDetails paymentDetails;
}

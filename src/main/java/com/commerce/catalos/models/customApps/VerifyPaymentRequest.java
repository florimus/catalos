package com.commerce.catalos.models.customApps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPaymentRequest {

    private Float amount;

    private String paymentId;

    private String paymentLink;
}

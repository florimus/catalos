package com.commerce.catalos.models.orders;

import com.commerce.catalos.core.enums.PaymentStatus;
import com.commerce.catalos.persistence.dtos.PaymentOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {

    private PaymentOption mode;

    private float amount;

    private String uniqueId;

    private String method;

    private String paymentId;

    private String paymentAt;

    private PaymentStatus status;
}

package com.commerce.catalos.models.orders;

import com.commerce.catalos.persistence.dtos.PaymentOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {

    private PaymentOption mode;

    private float amountDebited;

    private String uniqueId;

    private boolean success;
}

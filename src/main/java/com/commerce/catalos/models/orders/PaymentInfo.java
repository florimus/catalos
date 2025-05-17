package com.commerce.catalos.models.orders;

import com.commerce.catalos.core.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {

    private PaymentMode mode;

    private float amountDebited;

    private String uniqueId;

    private boolean success;
}

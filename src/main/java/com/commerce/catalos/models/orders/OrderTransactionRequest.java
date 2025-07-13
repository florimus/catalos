package com.commerce.catalos.models.orders;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderTransactionRequest {

    @NotBlank(message = "Payment id is mandatory")
    private String paymentId;

    @NotBlank(message = "paymentUniqueId id is mandatory")
    private String paymentUniqueId;
}

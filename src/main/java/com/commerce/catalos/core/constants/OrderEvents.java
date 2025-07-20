package com.commerce.catalos.core.constants;

public enum OrderEvents {
    Created,
    PaymentInitialised,
    Submitted,
    fulfillment,
    Invoiced,
    Shipped,
    OutOfDelivery,
    WaitingForReturnConfirmation,
    ReturnConfirmed,
    ReturnShipped,
    RefundConfirmed,
    Refunded,
    Cancelled,
    RefundCancelled
}

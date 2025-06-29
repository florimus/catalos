package com.commerce.catalos.models.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LineItemError {

    private String lineItemId;

    private String message;
}

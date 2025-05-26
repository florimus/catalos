package com.commerce.catalos.models.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductStatusUpdateResponse {

    private boolean status;

    private String message;
}

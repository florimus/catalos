package com.commerce.catalos.models.productTypes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTypeStatusUpdateResponse {

    private boolean status;

    private String message;
}

package com.commerce.catalos.models.brands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandStatusUpdateResponse {

    private String message;

    private boolean status;
}

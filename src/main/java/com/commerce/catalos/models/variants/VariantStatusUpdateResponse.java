package com.commerce.catalos.models.variants;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariantStatusUpdateResponse {

    private boolean status;

    private String message;
}

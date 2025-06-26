package com.commerce.catalos.models.taxes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxStatusUpdateResponse {

    private String message;

    private boolean status;
}

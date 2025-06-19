package com.commerce.catalos.models.categories;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryStatusUpdateResponse {

    private String message;

    private boolean status;
}

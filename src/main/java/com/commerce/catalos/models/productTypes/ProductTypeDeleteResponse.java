package com.commerce.catalos.models.productTypes;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductTypeDeleteResponse {

    private List<String> ids;

    private String message;
}

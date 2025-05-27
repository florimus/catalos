package com.commerce.catalos.models.products;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDeleteResponse {

    private List<String> ids;

    private String message;
}

package com.commerce.catalos.models.variants;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VariantDeleteResponse {

    private List<String> ids;

    private String message;
}

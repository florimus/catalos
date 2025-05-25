package com.commerce.catalos.models.productTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTypeListResponse {

    private String id;

    private String name;

    private String slug;

    private boolean active;
}

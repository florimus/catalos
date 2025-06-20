package com.commerce.catalos.models.brands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponse {

    private String id;

    private String name;

    private String avatar;

    private String seoTitle;

    private String seoDescription;

    private boolean active;
}

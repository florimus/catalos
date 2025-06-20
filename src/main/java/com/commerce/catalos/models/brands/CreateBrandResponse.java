package com.commerce.catalos.models.brands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBrandResponse {

    private String id;

    private String name;

    private String avatar;

    private String seoTitle;

    private String seoDescription;

    private boolean active;
}

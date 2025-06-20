package com.commerce.catalos.models.brands;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBrandRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    private String avatar;

    private String seoTitle;

    private String seoDescription;
}

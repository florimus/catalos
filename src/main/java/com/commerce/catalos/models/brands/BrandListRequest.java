package com.commerce.catalos.models.brands;

import lombok.Data;

import java.util.List;

@Data
public class BrandListRequest {

    private List<String> ids;
}

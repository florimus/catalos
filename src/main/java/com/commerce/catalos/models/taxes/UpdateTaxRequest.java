package com.commerce.catalos.models.taxes;

import lombok.Data;

@Data
public class UpdateTaxRequest {

    private String name;

    private boolean isFixed;

    private Float rate;
}

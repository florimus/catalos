package com.commerce.catalos.models.taxes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaxResponse {

    private String id;

    private String name;

    private boolean isFixed;

    private Float rate;

    private boolean active;
}

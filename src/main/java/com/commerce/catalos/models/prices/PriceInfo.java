package com.commerce.catalos.models.prices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceInfo {

    private List<String> taxClasses;

    private float salesPrice;
}

package com.commerce.catalos.models.orders;

import com.commerce.catalos.persistances.dtos.Discount;
import com.commerce.catalos.persistances.dtos.Product;
import com.commerce.catalos.persistances.dtos.Variant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {

    private String id;

    private Product product;

    private Variant variant;

    private Integer quantity;

    private LineItemPrice itemPrice;

}

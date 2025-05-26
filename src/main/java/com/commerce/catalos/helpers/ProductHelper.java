package com.commerce.catalos.helpers;

import com.commerce.catalos.models.products.CreateProductRequest;
import com.commerce.catalos.models.products.CreateProductResponse;
import com.commerce.catalos.persistances.dtos.Product;
import org.springframework.beans.BeanUtils;

public class ProductHelper {

    public static Product toProductFromCreateProductRequest(final CreateProductRequest createProductRequest) {
        Product product = new Product();
        BeanUtils.copyProperties(createProductRequest, product);
        return product;
    }

    public static CreateProductResponse toCreateProductResponseFromProduct(final Product product) {
        CreateProductResponse response = new CreateProductResponse();
        BeanUtils.copyProperties(product, response);
        return response;
    }
}

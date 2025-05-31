package com.commerce.catalos.helpers;

import com.commerce.catalos.models.products.CreateProductRequest;
import com.commerce.catalos.models.products.CreateProductResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.products.UpdateProductResponse;
import com.commerce.catalos.persistence.dtos.Product;

import org.springframework.beans.BeanUtils;

import java.util.List;

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

    public static ProductResponse toProductResponseProduct(final Product product) {
        ProductResponse response = new ProductResponse();
        BeanUtils.copyProperties(product, response);
        return response;
    }

    public static UpdateProductResponse toUpdateProductResponseFromProduct(final Product product) {
        UpdateProductResponse response = new UpdateProductResponse();
        BeanUtils.copyProperties(product, response);
        return response;
    }

    public static List<ProductResponse> toProductResponsesFromProducts(final List<Product> products) {
        return products.stream()
                .map(ProductHelper::toProductResponseProduct)
                .toList();
    }
}

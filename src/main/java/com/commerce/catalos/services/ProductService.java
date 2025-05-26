package com.commerce.catalos.services;

import com.commerce.catalos.models.products.CreateProductRequest;
import com.commerce.catalos.models.products.CreateProductResponse;
import com.commerce.catalos.models.products.ProductResponse;

public interface ProductService {

    public CreateProductResponse createProduct(final CreateProductRequest createProductRequest);

    public ProductResponse getProductById(final String id);
}

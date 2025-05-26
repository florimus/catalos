package com.commerce.catalos.services;

import com.commerce.catalos.models.products.CreateProductRequest;
import com.commerce.catalos.models.products.CreateProductResponse;

public interface ProductService {

    public CreateProductResponse createProduct(final CreateProductRequest createProductRequest);
}

package com.commerce.catalos.services;

import com.commerce.catalos.models.products.*;

public interface ProductService {

    public CreateProductResponse createProduct(final CreateProductRequest createProductRequest);

    public ProductResponse getProductById(final String id);

    public UpdateProductResponse updateProduct(final String id, final UpdateProductRequest updateProductRequest);

    public ProductStatusUpdateResponse updateProductStatus(final String id, final boolean status);
}

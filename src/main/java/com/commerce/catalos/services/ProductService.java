package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.products.*;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ProductService {

    public CreateProductResponse createProduct(final CreateProductRequest createProductRequest);

    public ProductResponse getProductById(final String id);

    public UpdateProductResponse updateProduct(final String id, final UpdateProductRequest updateProductRequest);

    public ProductStatusUpdateResponse updateProductStatus(final String id, final boolean status);

    public Page<ProductResponse> listProducts(final String query, final Pageable pageable);

    public ProductDeleteResponse deleteProducts(final String id);

    public List<ProductResponse> getProductsByIds(final List<String> ids);
}

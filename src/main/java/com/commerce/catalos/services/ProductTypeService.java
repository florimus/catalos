package com.commerce.catalos.services;

import com.commerce.catalos.models.productTypes.CreateProductTypeRequest;
import com.commerce.catalos.models.productTypes.CreateProductTypeResponse;
import com.commerce.catalos.models.productTypes.UpdateProductTypeRequest;
import com.commerce.catalos.models.productTypes.UpdateProductTypeResponse;
import jakarta.validation.Valid;

public interface ProductTypeService {

    public CreateProductTypeResponse createProductType(final CreateProductTypeRequest createProductTypeRequest);

    public UpdateProductTypeResponse updateProductType(final UpdateProductTypeRequest updateProductTypeRequest);
}

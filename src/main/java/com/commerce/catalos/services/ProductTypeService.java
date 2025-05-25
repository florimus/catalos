package com.commerce.catalos.services;

import com.commerce.catalos.models.productTypes.*;
import jakarta.validation.Valid;

public interface ProductTypeService {

    public CreateProductTypeResponse createProductType(final CreateProductTypeRequest createProductTypeRequest);

    public UpdateProductTypeResponse updateProductType(final UpdateProductTypeRequest updateProductTypeRequest);

    public ProductTypeResponse getProductTypeById(final String id);

    public ProductTypeStatusUpdateResponse updateProductTypeStatus(final String id, final boolean status);
}

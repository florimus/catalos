package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.productTypes.*;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface ProductTypeService {

    public CreateProductTypeResponse createProductType(final CreateProductTypeRequest createProductTypeRequest);

    public UpdateProductTypeResponse updateProductType(final UpdateProductTypeRequest updateProductTypeRequest);

    public ProductTypeResponse getProductTypeById(final String id);

    public ProductTypeStatusUpdateResponse updateProductTypeStatus(final String id, final boolean status);

    public Page<ProductTypeListResponse> listProductTypes(final String query, final Pageable pageable);
}

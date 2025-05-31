package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.productTypes.*;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductTypeService {

    public CreateProductTypeResponse createProductType(final CreateProductTypeRequest createProductTypeRequest);

    public UpdateProductTypeResponse updateProductType(final UpdateProductTypeRequest updateProductTypeRequest);

    public ProductTypeResponse getProductTypeById(final String id);

    public ProductTypeStatusUpdateResponse updateProductTypeStatus(final String id, final boolean status);

    public Page<ProductTypeListResponse> listProductTypes(final String query, final Pageable pageable);

    public ProductTypeDeleteResponse deleteProductTypes(final String id);

    public void validateProductAttributeValues(final String productTypeId,
            final Map<String, AttributeItemProperties> attributes);

    public void validateVariantAttributeValues(final String productTypeId,
            final Map<String, AttributeItemProperties> attributes);
}

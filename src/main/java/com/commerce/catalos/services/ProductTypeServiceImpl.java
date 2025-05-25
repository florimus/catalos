package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.helpers.ProductTypeHelper;
import com.commerce.catalos.models.productTypes.CreateProductTypeRequest;
import com.commerce.catalos.models.productTypes.CreateProductTypeResponse;
import com.commerce.catalos.models.productTypes.UpdateProductTypeRequest;
import com.commerce.catalos.models.productTypes.UpdateProductTypeResponse;
import com.commerce.catalos.persistances.dtos.ProductType;
import com.commerce.catalos.persistances.repositories.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    private boolean isSlugExits(final String slug) {
        return productTypeRepository.existsBySlug(slug);
    }

    @Override
    public CreateProductTypeResponse createProductType(CreateProductTypeRequest createProductTypeRequest) {
        if (this.isSlugExits(createProductTypeRequest.getSlug())) {
            Logger.error("1519cbdb-7f13-40ca-92c3-d48fe1efeab4", "Slug: {} is already exits", createProductTypeRequest.getSlug());
            throw new ConflictException("Slug is already exits");
        }

        ProductType productType = ProductTypeHelper.toProductTypeFromCreateProductTypeRequest(createProductTypeRequest);
        Logger.info("a6c14689-7693-4027-ac54-babc9eb68059", "Creating product type {}", createProductTypeRequest.getName());
        return ProductTypeHelper.toCreateProductTypeResponseFromProductType(productTypeRepository.save(productType));
    }

    @Override
    public UpdateProductTypeResponse updateProductType(UpdateProductTypeRequest updateProductTypeRequest) {
        Logger.info("658c9599-a80b-4f8c-adff-a63bf6a08d3c", "Start validating product attributes");
        ProductTypeHelper.validateAttributeItemPropertiesMap(updateProductTypeRequest.getProductAttributes());

        Logger.info("5ccc9722-6f72-476f-9f1b-78895a422e63", "Start validating variant attributes");
        ProductTypeHelper.validateAttributeItemPropertiesMap(updateProductTypeRequest.getVariantAttributes());

        ProductType productType = productTypeRepository.findProductTypeByIdAndEnabled(updateProductTypeRequest.getId(), true);
        if (!updateProductTypeRequest.getName().isBlank()) {
            productType.setName(updateProductTypeRequest.getName());
        }
        productType.setProductAttributes(updateProductTypeRequest.getProductAttributes());
        productType.setVariantAttributes(updateProductTypeRequest.getVariantAttributes());

        Logger.info("06cb9db2-f0a8-4a3d-a831-5a0e8e6095b0", "Saving Product type");
        return ProductTypeHelper.toUpdateProductTypeResponseFromProductType(productTypeRepository.save(productType));
    }
}

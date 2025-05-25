package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.ProductTypeHelper;
import com.commerce.catalos.models.productTypes.*;
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

    private ProductType findProductTypeById(final String productId) {
        return productTypeRepository.findProductTypeByIdAndEnabled(productId, true);
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

        ProductType productType = this.findProductTypeById(updateProductTypeRequest.getId());
        if (productType == null || !productType.getId().equals(updateProductTypeRequest.getId())) {
            Logger.error("a45fa66c-8fe5-4719-a785-e6d05c12f4ca", "Product type not found: {}", updateProductTypeRequest.getId());
            throw new NotFoundException("Product-type not found");
        }

        if (!updateProductTypeRequest.getName().isBlank()) {
            productType.setName(updateProductTypeRequest.getName());
        }
        productType.setProductAttributes(updateProductTypeRequest.getProductAttributes());
        productType.setVariantAttributes(updateProductTypeRequest.getVariantAttributes());

        Logger.info("06cb9db2-f0a8-4a3d-a831-5a0e8e6095b0", "Saving Product type");
        return ProductTypeHelper.toUpdateProductTypeResponseFromProductType(productTypeRepository.save(productType));
    }

    @Override
    public ProductTypeResponse getProductTypeById(final String id) {
        if (id.isBlank()) {
            Logger.error("6ae4d3ac-b32a-4c22-9579-4d3a17f12ea6", "Product-type id cannot be blank");
            throw new BadRequestException("Invalid product-type id");
        }
        ProductType productType = this.findProductTypeById(id);
        if (productType == null || !productType.getId().equals(id)) {
            Logger.error("e01f88c6-53cf-47bf-a478-38a606670dcd", "Product type not found: {}", id);
            throw new NotFoundException("Product-type not found");
        }
        Logger.info("7a8ecf2b-c389-4356-b492-90a6a2a6fb7e", "Updating product type by id: {}", id);
        return ProductTypeHelper.toProductTypeResponseFromProductType(productType);
    }

    @Override
    public ProductTypeStatusUpdateResponse updateProductTypeStatus(final String id, final boolean status) {
        if (id.isBlank()) {
            Logger.error("90e2e167-3b60-48da-bff3-eb60b4223327", "Product-type id cannot be blank");
            throw new BadRequestException("Invalid product-type id");
        }
        ProductType productType = this.findProductTypeById(id);
        if (productType == null || !productType.getId().equals(id)) {
            Logger.error("788276e7-da47-44ff-9b59-e38940289a69", "Product type not found: {}", id);
            throw new NotFoundException("Product-type not found");
        }
        productType.setActive(status);
        Logger.info(
                "318e4c67-4c4c-4a08-bc33-b02b61ce7a6c", status ? "Activating the product-type {}" : "Deactivating the product-type {}", id);
        productType = productTypeRepository.save(productType);
        return ProductTypeStatusUpdateResponse.builder()
                .status(productType.isActive())
                .message(productType.isActive() ? "Activated the product-type" : "Deactivated the product-type")
                .build();
    }
}

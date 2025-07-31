package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.ProductTypeHelper;
import com.commerce.catalos.models.productTypes.*;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.persistence.dtos.ProductType;
import com.commerce.catalos.persistence.repositories.ProductTypeRepository;
import com.commerce.catalos.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    private final AuthContext authContext;

    private boolean isSlugExits(final String slug) {
        return productTypeRepository.existsBySlug(slug);
    }

    private ProductType findProductTypeById(final String productId) {
        return productTypeRepository.findProductTypeByIdAndEnabled(productId, true);
    }

    @Override
    public CreateProductTypeResponse createProductType(CreateProductTypeRequest createProductTypeRequest) {
        if (this.isSlugExits(createProductTypeRequest.getSlug())) {
            Logger.error("1519cbdb-7f13-40ca-92c3-d48fe1efeab4", "Slug: {} is already exits",
                    createProductTypeRequest.getSlug());
            throw new ConflictException("Slug is already exits");
        }

        ProductType productType = ProductTypeHelper.toProductTypeFromCreateProductTypeRequest(createProductTypeRequest);
        GetUserInfoResponse userInfo = authContext.getCurrentUser();
        productType.setUpdatedAt(new Date());
        productType.setCreatedAt(new Date());
        productType.setCreatedBy(userInfo.getEmail());
        productType.setUpdatedBy(userInfo.getEmail());
        Logger.info("a6c14689-7693-4027-ac54-babc9eb68059", "Creating product type {}",
                createProductTypeRequest.getName());
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
            Logger.error("a45fa66c-8fe5-4719-a785-e6d05c12f4ca", "Product type not found: {}",
                    updateProductTypeRequest.getId());
            throw new NotFoundException("Product-type not found");
        }

        if (!updateProductTypeRequest.getName().isBlank()) {
            productType.setName(updateProductTypeRequest.getName());
        }
        productType.setProductAttributes(updateProductTypeRequest.getProductAttributes());
        productType.setVariantAttributes(updateProductTypeRequest.getVariantAttributes());

        GetUserInfoResponse userInfo = authContext.getCurrentUser();
        productType.setUpdatedAt(new Date());
        productType.setUpdatedBy(userInfo.getEmail());

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
        Logger.info("7a8ecf2b-c389-4356-b492-90a6a2a6fb7e", "fetching product type by id: {}", id);
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
        GetUserInfoResponse userInfo = authContext.getCurrentUser();
        productType.setActive(status);
        productType.setUpdatedAt(new Date());
        productType.setUpdatedBy(userInfo.getEmail());
        Logger.info(
                "318e4c67-4c4c-4a08-bc33-b02b61ce7a6c",
                status ? "Activating the product-type {}" : "Deactivating the product-type {}", id);
        productType = productTypeRepository.save(productType);
        return ProductTypeStatusUpdateResponse.builder()
                .status(productType.isActive())
                .message(productType.isActive() ? "Activated the product-type" : "Deactivated the product-type")
                .build();
    }

    @Override
    public Page<ProductTypeListResponse> listProductTypes(final String query, final Pageable pageable) {
        Logger.info("a7ff367d-1e60-4ab5-83e1-816a8a47c76b", "Finding product-types with query: {} and pageable: {}",
                query, pageable);
        Page<ProductType> productTypes = productTypeRepository.searchProductTypes(query, pageable);
        return new Page<ProductTypeListResponse>(
                ProductTypeHelper.toProductTypeListResponseFromProductTypes(productTypes.getHits()),
                productTypes.getTotalHitsCount(),
                productTypes.getCurrentPage(),
                productTypes.getPageSize());
    }

    @Override
    public ProductTypeDeleteResponse deleteProductTypes(final String id) {
        if (id.isBlank()) {
            Logger.error("23057ef3-11a8-4463-ad0c-605870113abe", "Product-type id cannot be blank");
            throw new BadRequestException("Invalid product-type id");
        }
        ProductType productType = this.findProductTypeById(id);
        if (productType == null || !productType.getId().equals(id)) {
            Logger.error("37e47d1c-31a7-4c38-8345-a5903dd14b26", "Product type not found: {}", id);
            throw new NotFoundException("Product-type not found");
        }
        GetUserInfoResponse userInfo = authContext.getCurrentUser();
        productType.setActive(false);
        productType.setEnabled(false);
        productType.setUpdatedAt(new Date());
        productType.setUpdatedBy(userInfo.getEmail());

        // TODO: Need to delete products in async.

        Logger.info("ac117033-bed7-4be0-8818-3dc837c03f3e", "Deleting product-type: {}", productType.getName());
        productTypeRepository.save(productType);
        return ProductTypeDeleteResponse.builder()
                .ids(List.of(id))
                .message("Product-type deleted successfully")
                .build();
    }

    @Override
    public void validateProductAttributeValues(final String productTypeId,
            final Map<String, AttributeItemProperties> attributes) {
        ProductType productType = this.findProductTypeById(productTypeId);
        if (productType == null || !productType.getId().equals(productTypeId)) {
            Logger.error("7b038a33-8dda-423f-ac53-4d6333cb159a", "Product type not found: {}", productTypeId);
            throw new NotFoundException("Product-type not found");
        }
        Logger.info("9f650cb0-8303-4eb5-bff0-5d00c1d1cf3f", "Product-type found, validating the product attributes");
        ProductTypeHelper.validateAttribute(productType.getProductAttributes(), attributes);
    }

    @Override
    public void validateVariantAttributeValues(final String productTypeId,
            final Map<String, AttributeItemProperties> attributes) {
        ProductType productType = this.findProductTypeById(productTypeId);
        if (productType == null || !productType.getId().equals(productTypeId)) {
            Logger.error("98db48fb-d146-4524-83f3-0c3722fcb800", "Product type not found: {}", productTypeId);
            throw new NotFoundException("Product-type not found");
        }
        Logger.info("e2491ede-0de0-4dfc-b6e8-68b311e291d3", "Product-type found, validating the variant attributes");
        ProductTypeHelper.validateAttribute(productType.getVariantAttributes(), attributes);
    }
}

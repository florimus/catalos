package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.ProductHelper;
import com.commerce.catalos.models.products.*;
import com.commerce.catalos.persistances.dtos.Product;
import com.commerce.catalos.persistances.repositories.ProductRepository;
import com.commerce.catalos.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductTypeService productTypeService;

    private final AuthContext authContext;

    private boolean isExitsWithSkuId(final String skuId) {
        return productRepository.existsBySkuIdAndEnabled(skuId, true);
    }

    private Product findProductById(final String productId) {
        return productRepository.findProductByIdAndEnabled(productId, true);
    }

    @Override
    public CreateProductResponse createProduct(final CreateProductRequest createProductRequest) {
        if (this.isExitsWithSkuId(createProductRequest.getSkuId())) {
            Logger.error("24ebdcf4-9a92-4d61-9462-0b8d40e15976", "Sku is already exits, cannot create new product");
            throw new ConflictException("Sku is already exits");
        }
        productTypeService.validateProductAttributeValues(createProductRequest.getProductTypeId(), createProductRequest.getAttributes());
        Logger.info("4177f797-1ef2-4623-aa00-052e2f2366fa", "Product attributes validated successfully");

        // TODO: Validate channel list

        Product product = ProductHelper.toProductFromCreateProductRequest(createProductRequest);
        product.setActive(true);
        product.setEnabled(true);
        product.setCreatedAt(new Date());
        product.setCreatedBy(authContext.getCurrentUser().getEmail());
        Logger.info("6454e09d-26a9-4b67-9759-c5584c706c02", "Saving product : {}", product.getName());
        return ProductHelper.toCreateProductResponseFromProduct(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(String id) {
        if (id.isBlank()) {
            Logger.error("08691593-3a98-4365-aa71-b43f218cd252", "Product id is mandatory");
            throw new BadRequestException("Invalid product id");
        }
        Product product = this.findProductById(id);
        if (product == null || product.getId().isBlank()){
            Logger.error("36f3dfdf-9bc5-41af-b390-ac31004fe416", "Product not found with id: {}", id);
            throw new NotFoundException("Product not found");
        }
        Logger.info("442703ab-4410-4bfd-8d3c-a2f0975f193f", "Product {} found", product.getName());
        return ProductHelper.toProductResponseProduct(product);
    }

    @Override
    public UpdateProductResponse updateProduct(String id, UpdateProductRequest updateProductRequest) {
        if (id.isBlank()) {
            Logger.error("699203ba-c2d3-4c01-b982-0384cad82c73", "Product id is mandatory");
            throw new BadRequestException("Invalid product id");
        }
        Product product = this.findProductById(id);
        if (product == null || product.getId().isBlank()){
            Logger.error("050853e2-fccc-44af-9dcf-a3b1cfd48a9f", "Product not found with id: {}", id);
            throw new NotFoundException("Product not found");
        }
        productTypeService.validateProductAttributeValues(product.getProductTypeId(), updateProductRequest.getAttributes());
        Logger.info("2927b858-c0c2-42b7-a45d-a5c94891c5e0", "Product attributes validated successfully");
        if (!updateProductRequest.getName().isBlank()) {
            product.setName(updateProductRequest.getName());
        }
        product.setCategoryId(updateProductRequest.getCategoryId());
        product.setBrandId(updateProductRequest.getBrandId());
        product.setAttributes(updateProductRequest.getAttributes());

        // TODO: Validate channel list

        product.setPublishedChannels(updateProductRequest.getPublishedChannels());
        product.setUpdatedAt(new Date());
        product.setUpdatedBy(authContext.getCurrentUser().getEmail());
        Logger.info("9d4ed3e0-9368-4bf3-bda2-9a8826203beb", "Updating product");
        return ProductHelper.toUpdateProductResponseFromProduct(productRepository.save(product));
    }

    @Override
    public ProductStatusUpdateResponse updateProductStatus(final String id, final boolean status) {
        if (id.isBlank()) {
            Logger.error("f061fb49-b52e-4697-ba04-c152fa470918", "Product id is mandatory");
            throw new BadRequestException("Invalid product id");
        }
        Product product = this.findProductById(id);
        if (product == null || product.getId().isBlank()){
            Logger.error("cb6b44da-2a53-48b1-b19f-a7f411c64fa5", "Product not found with id: {}", id);
            throw new NotFoundException("Product not found");
        }
        product.setActive(status);

        // TODO: Disable the variants if product disables

        Logger.info("667b88c4-3ce1-4bbf-83eb-e82c967c6880","Updating the status to {} for product: {}", status, product.getName());
        product = productRepository.save(product);
        return ProductStatusUpdateResponse.builder()
                .status(product.isActive())
                .message(product.isActive() ? "Product Activated": "Product Deactivated")
                .build();
    }
}

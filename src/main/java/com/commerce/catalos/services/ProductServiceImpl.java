package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.ProductHelper;
import com.commerce.catalos.models.brands.BrandResponse;
import com.commerce.catalos.models.categories.CategoryResponse;
import com.commerce.catalos.models.products.*;
import com.commerce.catalos.persistence.dtos.Product;
import com.commerce.catalos.persistence.repositories.ProductRepository;
import com.commerce.catalos.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductTypeService productTypeService;

    private final ChannelService channelService;

    private final CategoryService categoryService;

    private final BrandService brandService;

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
        productTypeService.validateProductAttributeValues(createProductRequest.getProductTypeId(),
                createProductRequest.getAttributes());
        Logger.info("4177f797-1ef2-4623-aa00-052e2f2366fa", "Product attributes validated successfully");

        channelService.verifyChannels(createProductRequest.getPublishedChannels(), true);

        Product product = ProductHelper.toProductFromCreateProductRequest(createProductRequest);

        if (createProductRequest.getCategoryId() != null && !createProductRequest.getCategoryId().isBlank()) {
            CategoryResponse category = categoryService.getCategory(createProductRequest.getCategoryId());
            if (category == null) {
                Logger.error("5fcdcbad-ac3b-4960-aa32-c6114e02b3e0", "Category not found with id: {}",
                        createProductRequest.getCategoryId());
                throw new NotFoundException("Category not found");
            }
            if (!category.isActive()) {
                Logger.error("991b59dc-93e0-43a2-a4ae-bdfa703d28cb", "Category is not active with id: {}",
                        createProductRequest.getCategoryId());
                throw new ConflictException("Category is not active");
            }
            product.setCategoryName(category.getName());
            product.setCategoryId(category.getId());
        }

        if (createProductRequest.getBrandId() != null && !createProductRequest.getBrandId().isBlank()) {
            BrandResponse brand = brandService.getBrandById(createProductRequest.getBrandId());
            if (brand == null) {
                Logger.error("3174a3c7-5aac-48cb-9ceb-3a0033aad0ac", "Brand not found with id: {}",
                        createProductRequest.getBrandId());
                throw new NotFoundException("Brand not found");
            }
            if (!brand.isActive()) {
                Logger.error("b7e015e5-b9bc-4240-bdbb-82d9cd73d325", "Brand is not active with id: {}",
                        createProductRequest.getBrandId());
                throw new ConflictException("Brand is not active");
            }
            product.setBrandId(brand.getId());
            product.setBrandName(brand.getName());
        }

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
        if (product == null || product.getId().isBlank()) {
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
        if (product == null || product.getId().isBlank()) {
            Logger.error("050853e2-fccc-44af-9dcf-a3b1cfd48a9f", "Product not found with id: {}", id);
            throw new NotFoundException("Product not found");
        }
        productTypeService.validateProductAttributeValues(product.getProductTypeId(),
                updateProductRequest.getAttributes());
        Logger.info("2927b858-c0c2-42b7-a45d-a5c94891c5e0", "Product attributes validated successfully");

        product.setAttributes(updateProductRequest.getAttributes());

        if (!updateProductRequest.getName().isBlank()) {
            product.setName(updateProductRequest.getName());
        }

        if (updateProductRequest.getCategoryId() != null && !updateProductRequest.getCategoryId().isBlank()) {
            CategoryResponse category = categoryService.getCategory(updateProductRequest.getCategoryId());
            if (category == null) {
                Logger.error("6268383e-8489-44bd-97a8-8704119a0a68", "Category not found with id: {}",
                        updateProductRequest.getCategoryId());
                throw new NotFoundException("Category not found");
            }
            if (!category.isActive()) {
                Logger.error("f54ea0db-6aa0-47db-af1f-1fa7c15dbaf7", "Category is not active with id: {}",
                        updateProductRequest.getCategoryId());
                throw new ConflictException("Category is not active");
            }
            product.setCategoryName(category.getName());
            product.setCategoryId(category.getId());
        }

        if (updateProductRequest.getBrandId() != null && !updateProductRequest.getBrandId().isBlank()) {
            BrandResponse brand = brandService.getBrandById(updateProductRequest.getBrandId());
            if (brand == null) {
                Logger.error("4ef8583f-45a7-40df-89e4-c4683b7196a3", "Brand not found with id: {}",
                        updateProductRequest.getBrandId());
                throw new NotFoundException("Brand not found");
            }
            if (!brand.isActive()) {
                Logger.error("1323ae62-07b0-47d0-95ca-f7651990daab", "Brand is not active with id: {}",
                        updateProductRequest.getBrandId());
                throw new ConflictException("Brand is not active");
            }
            product.setBrandId(brand.getId());
            product.setBrandName(brand.getName());
        }

        Logger.info("dfb48017-29fd-46b1-bd2f-d4f4a45add38", "Start validating channels: {}",
                updateProductRequest.getPublishedChannels());
        channelService.verifyChannels(updateProductRequest.getPublishedChannels(), true);

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
        if (product == null || product.getId().isBlank()) {
            Logger.error("cb6b44da-2a53-48b1-b19f-a7f411c64fa5", "Product not found with id: {}", id);
            throw new NotFoundException("Product not found");
        }
        product.setActive(status);

        // TODO: Disable the variants if product disables

        Logger.info("667b88c4-3ce1-4bbf-83eb-e82c967c6880", "Updating the status to {} for product: {}", status,
                product.getName());
        product = productRepository.save(product);
        return ProductStatusUpdateResponse.builder()
                .status(product.isActive())
                .message(product.isActive() ? "Product Activated" : "Product Deactivated")
                .build();
    }

    @Override
    public Page<ProductResponse> listProducts(final String query, final Pageable pageable) {
        Logger.info("07b20b95-bced-4a83-b946-e2e3cb9318b5", "Finding the products with query: {} and pagination: {}",
                query, pageable);
        Page<Product> products = productRepository.searchProducts(query, pageable);
        return new Page<ProductResponse>(
                ProductHelper.toProductResponsesFromProducts(products.getHits()),
                products.getTotalHitsCount(),
                products.getCurrentPage(),
                products.getPageSize());
    }

    @Override
    public ProductDeleteResponse deleteProducts(String id) {
        if (id.isBlank()) {
            Logger.error("534d92b2-6478-497e-bcb7-8b38e9f481fe", "Product id is mandatory");
            throw new BadRequestException("Invalid product id");
        }
        Product product = this.findProductById(id);
        if (product == null || product.getId().isBlank()) {
            Logger.error("e9df6268-3d1c-4c76-ab7d-46e4659f9720", "Product not found with id: {}", id);
            throw new NotFoundException("Product not found");
        }
        product.setActive(false);
        product.setEnabled(false);
        product.setUpdatedAt(new Date());
        product.setUpdatedBy(authContext.getCurrentUser().getEmail());
        productRepository.save(product);
        return ProductDeleteResponse.builder()
                .ids(List.of(id))
                .message("Product deleted successfully")
                .build();
    }

    @Override
    public List<ProductResponse> getProductsByIds(final List<String> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return products.stream()
                .filter(product -> product != null && product.getId() != null && !product.getId().isBlank())
                .map(ProductHelper::toProductResponseProduct)
                .toList();
    }

    @Override
    public Page<ProductResponse> getProductsByVariantId(final String categoryId, final String query,
            final Pageable pageable) {
        Logger.info("54e26fbc-564a-42a0-a78e-f32cb632b695",
                "Finding the products with category: {}, query: {} and pagination: {}",
                categoryId, query, pageable);
        Page<Product> products = productRepository.searchProductsWithCategory(categoryId, query, pageable);
        return new Page<ProductResponse>(
                ProductHelper.toProductResponsesFromProducts(products.getHits()),
                products.getTotalHitsCount(),
                products.getCurrentPage(),
                products.getPageSize());
    }
}

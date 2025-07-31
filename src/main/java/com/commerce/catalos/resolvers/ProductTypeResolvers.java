package com.commerce.catalos.resolvers;

import com.commerce.catalos.models.productTypes.ProductTypeResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.security.RequestContext;
import com.commerce.catalos.services.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductTypeResolvers {

    private final RequestContext requestContext;

    private final ProductTypeService productTypeService;

    @SchemaMapping(typeName = "Product", field = "productType")
    public ProductTypeResponse resolveProductTypeInProduct(final ProductResponse productResponse) {
        return this.productTypeService.getProductTypeById(productResponse.getProductTypeId());
    }

    @SchemaMapping(typeName = "Variant", field = "productType")
    public ProductTypeResponse resolveProductTypeInVariant(final VariantResponse variantResponse) {
        return this.productTypeService.getProductTypeById(variantResponse.getProductTypeId());
    }
}

package com.commerce.catalos.resolvers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.services.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VariantResolver {

    private final VariantService variantService;

    @SchemaMapping(typeName = "Product", field = "variants")
    public List<VariantResponse> resolveVariants(ProductResponse productResponse) {
        return variantService.getAllProductVariants(productResponse.getId());
    }

    @QueryMapping
    public VariantResponse getVariantById(@Argument("id") final String id) {
        Logger.info("",
                "Received request for fetch variant info by id : {}", id);
        return variantService.getVariantById(id);
    }
}

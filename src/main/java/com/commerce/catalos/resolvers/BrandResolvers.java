package com.commerce.catalos.resolvers;

import com.commerce.catalos.models.brands.BrandResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class BrandResolvers {

    private final BrandService brandService;

    @SchemaMapping(typeName = "Product", field = "brand")
    public BrandResponse resolveBrand(ProductResponse productResponse) {
        return brandService.getBrandById(productResponse.getBrandId());
    }
}

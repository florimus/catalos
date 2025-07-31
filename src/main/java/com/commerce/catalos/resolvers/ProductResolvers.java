package com.commerce.catalos.resolvers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductResolvers {

    private final ProductService productService;

    @QueryMapping
    public ProductResponse getProductById(@Argument("id") final String id){
        Logger.info("",
                "Received request for fetch product info by id : {}", id);
        return productService.getProductById(id);
    }
}

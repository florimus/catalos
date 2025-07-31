package com.commerce.catalos.resolvers;

import com.commerce.catalos.models.productTypes.ProductTypeResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.translations.TranslationResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.security.RequestContext;
import com.commerce.catalos.services.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TranslationResolvers {

    private final RequestContext requestContext;

    private final TranslationService translationService;

    @SchemaMapping(typeName = "Product", field = "translations")
    public Map<String, Object> resolveProductTranslations(final ProductResponse productResponse){
        if (requestContext.getLanguage().equals("EN")){
            return  null;
        }
        TranslationResponse translations = translationService.fetchTranslation(productResponse.getId(), requestContext.getLanguage());
        return translations.getTranslations();
    }

    @SchemaMapping(typeName = "ProductType", field = "translations")
    public Map<String, Object> resolveProductTypeTranslations(final ProductTypeResponse productTypeResponse){
        if (requestContext.getLanguage().equals("EN")){
            return  null;
        }
        TranslationResponse translations = translationService.fetchTranslation(productTypeResponse.getId(), requestContext.getLanguage());
        return translations.getTranslations();
    }

    @SchemaMapping(typeName = "Variant", field = "translations")
    public Map<String, Object> resolveVariantTranslations(final VariantResponse variantResponse){
        if (requestContext.getLanguage().equals("EN")){
            return null;
        }
        TranslationResponse translations = translationService.fetchTranslation(variantResponse.getId(), requestContext.getLanguage());
        if (null != translations){
            return translations.getTranslations();
        }
        return null;
    }
}

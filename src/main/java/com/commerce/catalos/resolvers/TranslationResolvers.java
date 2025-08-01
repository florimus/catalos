package com.commerce.catalos.resolvers;

import com.commerce.catalos.models.brands.BrandResponse;
import com.commerce.catalos.models.categories.CategoryResponse;
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
        return translations.getTranslations();
    }

    @SchemaMapping(typeName = "Brand", field = "translations")
    public Map<String, Object> resolveBrandTranslations(final BrandResponse brandResponse){
        if (requestContext.getLanguage().equals("EN")){
            return null;
        }
        TranslationResponse translations = translationService.fetchTranslation(brandResponse.getId(), requestContext.getLanguage());
        return translations.getTranslations();
    }

    @SchemaMapping(typeName = "Category", field = "translations")
    public Map<String, Object> resolveCategoryTranslations(final CategoryResponse categoryResponse){
        if (requestContext.getLanguage().equals("EN")){
            return null;
        }
        TranslationResponse translations = translationService.fetchTranslation(categoryResponse.getId(), requestContext.getLanguage());
        return translations.getTranslations();
    }
}

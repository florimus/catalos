package com.commerce.catalos.helpers;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.productTypes.ProductTypeResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.models.variants.VariantURLResponse;
import com.commerce.catalos.persistence.dtos.Variant;

public class VariantHelper {

    public static CreateVariantResponse toCreateVariantResponseFromVariant(final Variant variant) {
        CreateVariantResponse response = new CreateVariantResponse();
        BeanUtils.copyProperties(variant, response);
        return response;
    }

    public static VariantResponse toVariantResponseFromVariant(final Variant variant) {
        VariantResponse response = new VariantResponse();
        BeanUtils.copyProperties(variant, response);
        return response;
    }

    public static List<VariantListResponse> toProductTypeListResponseFromVariants(final List<Variant> variants) {
        return variants.stream().map(variant -> {
            VariantListResponse response = new VariantListResponse();
            BeanUtils.copyProperties(variant, response);
            return response;
        }).toList();
    }

    public static UpdateVariantResponse toUpdateVariantResponseFromVariant(final Variant variant) {
        UpdateVariantResponse response = new UpdateVariantResponse();
        BeanUtils.copyProperties(variant, response);
        return response;
    }

    public static VariantURLResponse toVariantURLResponseFromVariant(final Variant variant,
            final ProductResponse product, ProductTypeResponse productType, final CalculatedPriceResponse prices) {
        VariantURLResponse response = new VariantURLResponse();
        BeanUtils.copyProperties(variant, response);
        response.setProductInfo(product);
        response.setProductType(productType);
        response.setPrices(prices);
        return response;
    }

    public static List<VariantResponse> toVariantListResponse(final List<Variant> variants) {
        return variants.stream().map(VariantHelper::toVariantResponseFromVariant).toList();
    }
}

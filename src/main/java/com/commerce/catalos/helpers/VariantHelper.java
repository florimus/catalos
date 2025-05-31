package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.VariantResponse;
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
}

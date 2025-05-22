package com.commerce.catalos.helpers;

import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import com.commerce.catalos.models.productTypes.CreateProductTypeRequest;
import com.commerce.catalos.models.productTypes.CreateProductTypeResponse;
import com.commerce.catalos.persistances.dtos.ProductType;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

public class ProductTypeHelper {

    public static CreateProductTypeResponse toCreateProductTypeResponseFromProductType(final ProductType productType) {
        CreateProductTypeResponse response = new CreateProductTypeResponse();
        BeanUtils.copyProperties(productType, response);
        return response;
    }

    public static ProductType toProductTypeFromCreateProductTypeRequest(final CreateProductTypeRequest createProductTypeRequest){
        ProductType productType = new ProductType();
        BeanUtils.copyProperties(createProductTypeRequest, productType);
        return productType;
    }

    public static void validatePlaneAttributes(final AttributeItemProperties attributeItemProperties) {
        attributeItemProperties.setOptions(null);
        attributeItemProperties.setValue(null);
    }

    public static void validateOptionAttributes(final AttributeItemProperties attributeItemProperties) {
        List<Map<String, String>> options;
        try {
            options = (List<Map<String, String>>) attributeItemProperties.getOptions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(options.isEmpty()){
            throw new BadRequestException("Options is mandatory for type: " + attributeItemProperties.getType());
        }
    }

    public static void validateAttributeItemPropertiesMap(
            final Map<String, AttributeItemProperties> attributeItemPropertiesMap){
        attributeItemPropertiesMap.forEach((key, value) -> {
            switch (value.getType()) {
                case Text, Number -> ProductTypeHelper.validatePlaneAttributes(value);
                case Select -> ProductTypeHelper.validateOptionAttributes(value);
            }
        });
    }
}

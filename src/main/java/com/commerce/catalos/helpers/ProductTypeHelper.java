package com.commerce.catalos.helpers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.models.productTypes.*;
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

    public static ProductType toProductTypeFromCreateProductTypeRequest(final CreateProductTypeRequest createProductTypeRequest) {
        ProductType productType = new ProductType();
        BeanUtils.copyProperties(createProductTypeRequest, productType);
        return productType;
    }

    public static UpdateProductTypeResponse toUpdateProductTypeResponseFromProductType(final ProductType productType) {
        UpdateProductTypeResponse response = new UpdateProductTypeResponse();
        BeanUtils.copyProperties(productType, response);
        return response;
    }

    public static ProductTypeResponse toProductTypeResponseFromProductType(final ProductType productType) {
        ProductTypeResponse response = new ProductTypeResponse();
        BeanUtils.copyProperties(productType, response);
        return response;
    }


    public static void validatePlaneAttributes(final AttributeItemProperties attributeItemProperties) {
        attributeItemProperties.setOptions(null);
        attributeItemProperties.setValue(null);
    }

    public static void validateOptionAttributes(final AttributeItemProperties attributeItemProperties) {
        List<Map<String, String>> options;
        try {
            options = (List<Map<String, String>>) attributeItemProperties.getOptions();
        } catch (ClassCastException e) {
            Logger.error("97c2c9e6-1812-4a2f-bdb4-206eef4e1014", "Options could not be cast to List<AttributeItem>");
            throw new BadRequestException("Invalid options format");
        }

        if (options == null || options.isEmpty()) {
            Logger.error("293563fb-06c2-4dfd-afd7-297616a83e0c", "Options cannot be empty, for type: {}", attributeItemProperties.getType());
            throw new BadRequestException("Options are mandatory for type: " + attributeItemProperties.getType());
        }

        for (Map<String, String> option : options) {
            if (option.get("label") == null || option.get("label").isBlank()
                    || option.get("value") == null || option.get("value").isBlank()) {
                Logger.error("ff4b8fe9-7d6f-44f8-b4c7-46595f82376a", "Label and Value should be defined for option: {}", option);
                throw new BadRequestException("Each option must have a non-blank label and value");
            }
        }
    }

    public static void validateAttributeItemPropertiesMap(
            final Map<String, AttributeItemProperties> attributeItemPropertiesMap){
        attributeItemPropertiesMap.forEach((key, value) -> {
            switch (value.getType()) {
                case Text, Number, Boolean -> ProductTypeHelper.validatePlaneAttributes(value);
                case Select, Multi_select -> ProductTypeHelper.validateOptionAttributes(value);
            }
        });
        Logger.info("1792c6af-afe7-40a1-bf9b-cf5acdc8a730", "Attribute items properties validated.");
    }
}

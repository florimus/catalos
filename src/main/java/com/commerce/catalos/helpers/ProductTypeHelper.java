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

    public static List<ProductTypeListResponse> toProductTypeListResponseFromProductTypes(final List<ProductType> productTypes) {
        return productTypes.stream()
                .map(productType -> ProductTypeListResponse.builder()
                        .id(productType.getId())
                        .slug(productType.getSlug())
                        .active(productType.isActive())
                        .name(productType.getName())
                        .build())
                .toList();
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

    public static void validateNumberAttributeValue(final String key, AttributeItemProperties values){
        if (!(values.getValue() instanceof Number)){
            Logger.error("002b7609-4a36-40a9-b037-cf20ab37e10f", "{}: Value should be number type", key);
            throw new BadRequestException("Invalid value type for : " + key);
        }
        values.setOptions(null);
    }

    public static void validateTextAttributeValue(final String key, final AttributeItemProperties values){
        if (!(values.getValue() instanceof String)){
            Logger.error("19857f1d-4046-4354-92f1-e96130e4092c", "{}: Value should be string type", key);
            throw new BadRequestException("Invalid value type for : " + key);
        }
        values.setOptions(null);
    }

    public static void validateBooleanAttributeValue(final String key, final AttributeItemProperties values){
        if (!(values.getValue() instanceof Boolean)){
            Logger.error("cba2526b-4cf7-4881-a848-0e856a08ff5a", "{}: Value should be boolean type", key);
            throw new BadRequestException("Invalid value type for : "+ key);
        }
        values.setOptions(null);
    }

    private static void validateSingleOptionAttribute(
            final String key, final AttributeItemProperties attributeItemProperties, final AttributeItemProperties values) {

        List<Map<String, String>> options;
        Map<String, String> value;

        try {
            options = (List<Map<String, String>>) attributeItemProperties.getOptions();
            value = (Map<String, String>) values.getValue();
        } catch (ClassCastException e) {
            Logger.error("f252c5ee-4763-4390-80cf-fb46f666d437", "{}: Failed to cast options or value", key);
            throw new BadRequestException("Invalid options format for attribute : " + key);
        }

        boolean matchFound = options.stream()
                .anyMatch(option ->
                        option.get("label").equals(value.get("label")) &&
                                option.get("value").equals(value.get("value"))
                );

        if (!matchFound) {
            Logger.error("f252c5ee-4763-4390-80cf-fb46f666d437", "{}: Provided value does not match any allowed options", key);
            throw new BadRequestException("Provided value is not in the list of allowed options in " + key);
        }
    }


    public static void validateAttribute (
            final Map<String, AttributeItemProperties> attributeItemPropertiesMap, final Map<String, AttributeItemProperties> attributes) {
        attributes.forEach((key, value) -> {
            switch (value.getType()) {
                case Text-> ProductTypeHelper.validateTextAttributeValue(key, value);
                case Number -> ProductTypeHelper.validateNumberAttributeValue(key, value);
                case Boolean -> ProductTypeHelper.validateBooleanAttributeValue(key, value);
                case Select -> ProductTypeHelper.validateSingleOptionAttribute(key, attributeItemPropertiesMap.get(key), value);
            }
        });
    }
}

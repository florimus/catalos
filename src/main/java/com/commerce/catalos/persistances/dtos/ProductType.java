package com.commerce.catalos.persistances.dtos;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document("cat_product_type")
@EqualsAndHashCode(callSuper = true)
public class ProductType extends BaseDto {

    @Id
    private String id;

    private String name;

    private String slug;

    private Map<String, AttributeItemProperties> productAttributes;

    private Map<String, AttributeItemProperties> variantAttributes;
}

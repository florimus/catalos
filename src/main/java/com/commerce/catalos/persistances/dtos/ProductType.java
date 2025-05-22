package com.commerce.catalos.persistances.dtos;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document("cat_product_type")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductType extends BaseDto {

    @Id
    private String id;

    private String name;

    private String slug;

    private Map<String, AttributeItemProperties> productAttributes;

    private Map<String, AttributeItemProperties> variantAttributes;
}

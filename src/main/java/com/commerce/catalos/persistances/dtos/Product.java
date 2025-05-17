package com.commerce.catalos.persistances.dtos;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("cat_product")
public class Product extends BaseDto {

    @Id
    private String id;

    private String name;

    private String skuId;

    private String categoryId;

    private String brandId;

    private String productTypeId;

    private List<String> publishedChannels;

    private Map<String, AttributeItemProperties> attributes;
}

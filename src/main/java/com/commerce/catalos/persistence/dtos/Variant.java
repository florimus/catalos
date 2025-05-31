package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import com.commerce.catalos.models.variants.ProductMedia;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Document("cat_variants")
@EqualsAndHashCode(callSuper = true)
public class Variant extends BaseDto {

    @Id
    private String id;

    private String name;

    private String parentId;

    private String productTypeId;

    private String slug;

    private String url;

    private String seoTitle;

    private String seoDescription;

    private List<ProductMedia> medias;

    private String skuId;

    private Map<String, AttributeItemProperties> attributes;
}

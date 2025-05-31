package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.core.enums.DiscountMode;
import com.commerce.catalos.core.enums.DiscountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("cat_variant")
@EqualsAndHashCode(callSuper = true)
public class Discount extends BaseDto {

    @Id
    private String id;

    private String name;

    private DiscountMode discountMode;

    private DiscountType discountType;

    private Float maxDiscountPrice;

    private List<String> discountedProducts;

    @Builder.Default
    private Integer minItemQuantity = 1;

    private boolean allProducts;

    private List<String> targetedProductIds;

    private List<String> targetedVariantIds;

    private List<String> targetedCategories;

    private List<String> targetedBrands;

    private List<String> targetedCollections;

    private String targetedUserGroup;

    private Date startDate;

    private Date expireDate;

    private String availableChannel;
}

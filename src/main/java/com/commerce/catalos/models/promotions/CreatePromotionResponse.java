package com.commerce.catalos.models.promotions;

import java.util.Date;
import java.util.List;

import com.commerce.catalos.core.enums.DiscountMode;
import com.commerce.catalos.core.enums.DiscountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePromotionResponse {

    private String id;

    private String name;

    private DiscountMode discountMode;

    private DiscountType discountType;

    private Float discountValue;

    private Float maxDiscountPrice;

    private List<String> discountedProducts;

    private Integer minItemQuantity;

    private boolean forAllProducts;

    private List<String> targetedProductIds;

    private List<String> targetedVariantIds;

    private List<String> targetedCategories;

    private List<String> targetedBrands;

    private List<String> targetedCollections;

    private String targetedUserGroup;

    private Date startDate;

    private Date expireDate;

    private String availableChannel;

    private boolean active;
}

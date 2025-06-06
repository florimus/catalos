package com.commerce.catalos.models.promotions;

import java.util.Date;
import java.util.List;

import com.commerce.catalos.core.enums.DiscountMode;
import com.commerce.catalos.core.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePromotionRequest {

    @NotBlank(message = "name is mandatory")
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Kolkata")
    private Date expireDate;

    @NotBlank(message = "availableChannel is mandatory")
    private String availableChannel;
}

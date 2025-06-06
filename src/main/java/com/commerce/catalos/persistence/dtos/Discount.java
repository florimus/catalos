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
@Document("cat_discounts")
@EqualsAndHashCode(callSuper = true)
public class Discount extends BaseDto {

    @Id
    private String id;

    private String name;

    private DiscountMode discountMode;

    private DiscountType discountType;

    private Float discountValue;

    private Float maxDiscountPrice;

    @Builder.Default
    private List<String> discountedProducts = List.of();

    @Builder.Default
    private Integer minItemQuantity = 1;

    private boolean forAllProducts;

    @Builder.Default
    private List<String> targetedProductIds = List.of();

    @Builder.Default
    private List<String> targetedVariantIds = List.of();

    @Builder.Default
    private List<String> targetedCategories = List.of();

    @Builder.Default
    private List<String> targetedBrands = List.of();

    @Builder.Default
    private List<String> targetedCollections = List.of();

    private String targetedUserGroup;

    private Date startDate;

    private Date expireDate;

    private String availableChannel;
}

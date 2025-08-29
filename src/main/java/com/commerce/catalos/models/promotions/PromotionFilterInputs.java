package com.commerce.catalos.models.promotions;

import com.commerce.catalos.core.enums.DiscountMode;
import com.commerce.catalos.core.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionFilterInputs {

    private Date startDate;

    private Date expireDate;

    private DiscountMode discountMode;

    private DiscountType discountType;

    private String targetedUserGroup;

    private boolean forAllProducts;
}

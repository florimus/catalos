package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.models.prices.PriceInfo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document("cat_pricing")
@EqualsAndHashCode(callSuper = true)
public class Price extends BaseDto {

    @Id
    private String id;

    private String skuId;

    private Map<String, PriceInfo> priceInfo;
}

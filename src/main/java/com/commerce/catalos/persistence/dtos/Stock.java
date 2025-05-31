package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.models.stocks.StockInfo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document("cat_variant")
@EqualsAndHashCode(callSuper = true)
public class Stock extends BaseDto {
    @Id
    private String id;

    private String variantId;

    private Map<String, StockInfo> stockInfo;
}

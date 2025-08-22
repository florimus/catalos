package com.commerce.catalos.persistence.dtos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("cat_dashboard")
public class Dashboard extends BaseDto {

    @Id
    private String id;

    private Integer year;

    private Integer initialCustomersCount;

    private Integer newCustomersCount;

    private Integer initialOrdersCount;

    private Integer newOrdersCount;

    private Float monthlyRevenue;

    private Float draftOrderRevenue;

    private Integer draftOrderCount;

    private Map<String, Float> monthlySales;

    private Map<String, Integer> channelSalesReport;
}

package com.commerce.catalos.models.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

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

    private boolean active;
}

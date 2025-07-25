package com.commerce.catalos.models.orders;

import com.commerce.catalos.core.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderFilterInputs {

    private String fromDate;

    private String toDate;

    private List<OrderStatus> statuses;

    private boolean excludeStatuses;
}

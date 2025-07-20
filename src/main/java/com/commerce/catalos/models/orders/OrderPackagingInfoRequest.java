package com.commerce.catalos.models.orders;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OrderPackagingInfoRequest {

    private Map<String, List<String>> unitIds;

    private Map<String, List<String>> packageIds;

}

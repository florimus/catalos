package com.commerce.catalos.models.taxes;

import java.util.List;

import lombok.Data;

@Data
public class UpdateTaxRequest {

    private String name;

    private boolean isFixed;

    private Float rate;

    private List<String> applicableChannels;
}

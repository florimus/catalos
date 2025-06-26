package com.commerce.catalos.models.taxes;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxResponse {

    private String id;

    private String name;

    private boolean isFixed;

    private Float rate;

    private boolean active;

    private List<String> applicableChannels;
}

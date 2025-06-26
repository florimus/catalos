package com.commerce.catalos.models.taxes;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaxRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    private boolean isFixed;

    @NotNull(message = "rate is mandatory")
    private Float rate;

    private List<String> applicableChannels;
}

package com.commerce.catalos.persistances.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {

    private boolean active;

    private boolean enabled;

    private String createdBy;

    private String updatedBy;

    private Date createdAt;

    private Date updatedAt;
}

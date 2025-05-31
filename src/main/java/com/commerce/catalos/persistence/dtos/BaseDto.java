package com.commerce.catalos.persistence.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {

    private boolean active = true;

    private boolean enabled = true;

    private String createdBy;

    private String updatedBy;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();
}

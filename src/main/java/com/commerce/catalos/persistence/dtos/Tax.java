package com.commerce.catalos.persistence.dtos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("cat_taxes")
public class Tax extends BaseDto {

    @Id
    private String id;

    private String name;

    private boolean isFixed;

    private Float rate;
}

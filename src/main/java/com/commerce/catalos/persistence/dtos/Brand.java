package com.commerce.catalos.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("cat_brands")
public class Brand extends BaseDto {

    @Id
    private String id;

    private String name;

    private String avatar;

    private String seoTitle;

    private String seoDescription;
}

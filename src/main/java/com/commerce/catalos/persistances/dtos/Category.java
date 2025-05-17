package com.commerce.catalos.persistances.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("cat_category")
public class Category extends BaseDto {

    @Id
    private String id;

    private String name;

    private String parentId;

    private String seoTitle;

    private String seoDescription;
}

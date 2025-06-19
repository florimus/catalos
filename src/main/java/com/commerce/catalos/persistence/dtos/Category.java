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
@NoArgsConstructor
@AllArgsConstructor
@Document("cat_categories")
public class Category extends BaseDto {

    @Id
    private String id;

    private String name;

    private String parentName;

    private String parentId;

    private String seoTitle;

    private String seoDescription;
}

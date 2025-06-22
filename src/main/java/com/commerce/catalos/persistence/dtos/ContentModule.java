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
@AllArgsConstructor
@NoArgsConstructor
@Document("cat_modules")
@EqualsAndHashCode(callSuper = true)
public class ContentModule extends BaseDto {

    @Id
    private String id;

    private String resourceId;

    private String data;
}

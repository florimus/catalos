package com.commerce.catalos.persistances.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("cat_variant")
@EqualsAndHashCode(callSuper = true)
public class Channel extends BaseDto {

    @Id
    private String id;

    private String name;

    private String slug;
}

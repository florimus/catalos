package com.commerce.catalos.persistence.dtos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("cat_api_keys")
public class APIKey extends BaseDto {

    @Id
    private String id;

    private String name;

    private String apiKey;

    private String apiSecret;

    private String roleId;
}

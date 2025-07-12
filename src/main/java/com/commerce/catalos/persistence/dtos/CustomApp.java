package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.core.enums.CustomAppType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("cat_custom_apps")
@EqualsAndHashCode(callSuper = true)
public class CustomApp extends BaseDto {

    @Id
    private String id;

    private String primaryKey;

    private String name;

    private String logo;

    private String description;

    private CustomAppType appType;

    private String connectionUrl;

    private List<String> applicableChannels;
}

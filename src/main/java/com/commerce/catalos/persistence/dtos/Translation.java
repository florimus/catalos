package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.core.enums.TranslationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document("cat_translations")
public class Translation extends BaseDto {

    @Id
    private String id;

    private String languageCode;

    private String uniqueId;

    private TranslationType type;

    private Map<String, Object> translations;
}

package com.commerce.catalos.models.translations;

import com.commerce.catalos.core.enums.TranslationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResponse {

    private String id;

    private String LanguageCode;

    private String uniqueId;

    private TranslationType type;

    private Map<String, Object> translations;

    private boolean active;
}

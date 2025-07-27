package com.commerce.catalos.models.translations;

import com.commerce.catalos.core.enums.TranslationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class TranslationRequest {

    @NotBlank(message = "Language is mandatory")
    private String languageCode;

    @NotBlank(message = "Unique id is mandatory")
    private String uniqueId;

    private TranslationType type;

    private Map<String, Object> translations;
}

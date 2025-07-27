package com.commerce.catalos.helpers;

import com.commerce.catalos.models.translations.TranslationRequest;
import com.commerce.catalos.models.translations.TranslationResponse;
import com.commerce.catalos.persistence.dtos.Translation;
import org.springframework.beans.BeanUtils;

public class TranslationHelper {

    public static Translation toTranslationFromTranslationRequest(final TranslationRequest translationRequest){
        Translation translation = new Translation();
        BeanUtils.copyProperties(translationRequest, translation);
        return translation;
    }

    public static TranslationResponse toTranslationResponseFromTranslation(final Translation translation) {
        TranslationResponse response = new TranslationResponse();
        BeanUtils.copyProperties(translation, response);
        return response;
    }
}

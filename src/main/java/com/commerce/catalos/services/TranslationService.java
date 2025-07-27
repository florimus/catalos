package com.commerce.catalos.services;

import com.commerce.catalos.models.translations.TranslationRequest;
import com.commerce.catalos.models.translations.TranslationResponse;

public interface TranslationService {

    TranslationResponse upsertTranslation(final TranslationRequest translationRequest);

    TranslationResponse fetchTranslation(final String uniqueId, final String languageCode);
}

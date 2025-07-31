package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.TranslationHelper;
import com.commerce.catalos.models.translations.TranslationRequest;
import com.commerce.catalos.models.translations.TranslationResponse;
import com.commerce.catalos.persistence.dtos.Translation;
import com.commerce.catalos.persistence.repositories.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;

    private Translation findTranslationByUniqueIdAndLanguageCode(final String uniqueId, final String languageCode) {
        return translationRepository.findTranslationByUniqueIdAndLanguageCodeAndActiveAndEnabled(uniqueId, languageCode, true, true);
    }

    @Override
    public TranslationResponse upsertTranslation(final TranslationRequest translationRequest) {
        Translation existingTranslation = this.findTranslationByUniqueIdAndLanguageCode(translationRequest.getUniqueId(), translationRequest.getLanguageCode());
        if (null == existingTranslation) {
            Logger.info("", "Creating new translation for {} in language:", translationRequest.getType(), translationRequest.getLanguageCode());
            Translation translation = translationRepository.save(TranslationHelper.toTranslationFromTranslationRequest(translationRequest));
            return TranslationHelper.toTranslationResponseFromTranslation(translation);
        }
        Logger.info("", "Translation already exits {}", existingTranslation.getId());
        existingTranslation.setTranslations(translationRequest.getTranslations());
        Translation translation = translationRepository.save(existingTranslation);
        Logger.info("", "Updating translation {}", existingTranslation.getId());
        return TranslationHelper.toTranslationResponseFromTranslation(translation);
    }

    @Override
    public TranslationResponse fetchTranslation(String uniqueId, String languageCode) {
        Translation translation = this.findTranslationByUniqueIdAndLanguageCode(uniqueId, languageCode);
        if (null == translation){
            Logger.error("","No translation for {} in language {}", uniqueId, languageCode);
            return null;
        }
        return TranslationHelper.toTranslationResponseFromTranslation(translation);
    }
}

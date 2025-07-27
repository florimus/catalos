package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.Translation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TranslationRepository extends MongoRepository<Translation, String> {

    Translation findTranslationByUniqueIdAndLanguageCodeAndActiveAndEnabled(
            final String uniqueId, final String languageCode, final boolean active, final boolean enabled);

}

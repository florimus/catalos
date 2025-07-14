package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.CustomApp;
import com.commerce.catalos.persistence.repositories.custom.CustomAppCustomRepository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomAppRepository extends MongoRepository<CustomApp, String>, CustomAppCustomRepository {

    CustomApp findCustomAppByPrimaryKeyAndApplicableChannelsInAndEnabledAndActive(final String primaryKey,
            final String channelId, final boolean enabled, boolean active);

    CustomApp findCustomAppByPrimaryKeyAndEnabledAndActive(final String primaryKey, final boolean enabled,
            final boolean active);

    CustomApp findCustomAppByIdAndEnabled(final String id, final boolean enabled);
}

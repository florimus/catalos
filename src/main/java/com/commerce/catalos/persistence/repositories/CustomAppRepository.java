package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.CustomApp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomAppRepository extends MongoRepository<CustomApp, String> {

    CustomApp findCustomAppByPrimaryKeyAndApplicableChannelsInAndEnabledAndActive(final String primaryKey, final String channelId, final boolean enabled, boolean active);
}

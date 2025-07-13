package com.commerce.catalos.persistence.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.PaymentOption;

public interface PaymentOptionRepository extends MongoRepository<PaymentOption, String> {

    List<PaymentOption> findPaymentOptionByApplicableChannelsInAndEnabledAndActive(final String channelId, final boolean enabled,
            final boolean active);

    PaymentOption findPaymentOptionByIdAndApplicableChannelsInAndEnabledAndActive(
            final String id, final String channel, final boolean enabled, final boolean active);
}

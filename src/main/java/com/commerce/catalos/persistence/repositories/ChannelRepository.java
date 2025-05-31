package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Channel;

import java.util.List;

public interface ChannelRepository extends MongoRepository<Channel, String> {

    List<Channel> findChannelsByIdAndEnabled(final List<String> ids, final boolean enabled);
}

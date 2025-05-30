package com.commerce.catalos.persistances.repositories;


import com.commerce.catalos.persistances.dtos.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChannelRepository extends MongoRepository<Channel, String> {

    List<Channel> findChannelsByIdAndEnabled(final List<String> ids, final boolean enabled);
}

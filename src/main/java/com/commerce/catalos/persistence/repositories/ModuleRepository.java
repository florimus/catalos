package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.ContentModule;

public interface ModuleRepository extends MongoRepository<ContentModule, String> {

    ContentModule findContentModuleByResourceId(final String resourceId);

}

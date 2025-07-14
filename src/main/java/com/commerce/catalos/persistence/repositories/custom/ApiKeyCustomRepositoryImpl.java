package com.commerce.catalos.persistence.repositories.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.APIKey;

@Repository
public class ApiKeyCustomRepositoryImpl implements ApiKeyCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<APIKey> searchApiKeys(final String search, final Pageable pageable) {
        Query query = new Query();

        if (search != null && !search.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<Criteria>();

            criteriaList.add(Criteria.where("apiKey").regex(search, "i"));
            criteriaList.add(Criteria.where("name").regex(search, "i"));

            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }
        query.addCriteria(new Criteria("enabled").is(true));
        long total = mongoTemplate.count(query, APIKey.class);
        query.with(pageable);
        List<APIKey> apiKeys = mongoTemplate.find(query, APIKey.class);
        return new Page<APIKey>(apiKeys, total, pageable.getPageNumber(), pageable.getPageSize());
    }

}

package com.commerce.catalos.persistence.repositories.custom;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.ProductType;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductTypeCustomRepositoryImpl implements ProductTypeCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<ProductType> searchProductTypes(String search, Pageable pageable) {
        Query query = new Query();

        if (search != null && !search.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<>();

            if (ObjectId.isValid(search)) {
                criteriaList.add(Criteria.where("id").is(new ObjectId(search)));
            }

            criteriaList.add(Criteria.where("name").regex(search, "i"));
            criteriaList.add(Criteria.where("slug").regex(search, "i"));

            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }
        query.addCriteria(new Criteria("enabled").is(true));

        long total = mongoTemplate.count(query, ProductType.class);
        query.with(pageable);
        List<ProductType> productTypes = mongoTemplate.find(query, ProductType.class);
        return new Page<ProductType>(productTypes, total, pageable.getPageNumber(), pageable.getPageSize());
    }
}

package com.commerce.catalos.persistence.repositories.custom;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Variant;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VariantCustomRepositoryImpl implements VariantCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Variant> searchProductVariants(final String productId, final String search, final Pageable pageable) {
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

        query.addCriteria(new Criteria("productId").is(productId));
        long total = mongoTemplate.count(query, Variant.class);
        query.with(pageable);
        List<Variant> productTypes = mongoTemplate.find(query, Variant.class);
        return new Page<Variant>(productTypes, total, pageable.getPageNumber(), pageable.getPageSize());
    }

}

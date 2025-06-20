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
import com.commerce.catalos.persistence.dtos.Brand;

@Repository
public class BrandCustomRepositoryImpl implements BrandCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Brand> searchBrands(final String search, final Pageable pageable) {
        Query query = new Query();

        if (search != null && !search.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<Criteria>();

            if (ObjectId.isValid(search)) {
                criteriaList.add(Criteria.where("id").is(new ObjectId(search)));
            }

            criteriaList.add(Criteria.where("name").regex(search, "i"));

            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }
        query.addCriteria(new Criteria("enabled").is(true));
        long total = mongoTemplate.count(query, Brand.class);
        query.with(pageable);
        List<Brand> products = mongoTemplate.find(query, Brand.class);
        return new Page<Brand>(products, total, pageable.getPageNumber(), pageable.getPageSize());
    }

}

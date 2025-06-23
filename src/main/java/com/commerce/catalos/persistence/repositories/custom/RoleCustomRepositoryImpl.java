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
import com.commerce.catalos.persistence.dtos.Role;

@Repository
public class RoleCustomRepositoryImpl implements RoleCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Role> searchRoles(String search, Pageable pageable) {
        Query query = new Query();

        if (search != null && !search.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<Criteria>();

            if (ObjectId.isValid(search)) {
                criteriaList.add(Criteria.where("id").is(new ObjectId(search)));
            }

            criteriaList.add(Criteria.where("name").regex(search, "i"));
            criteriaList.add(Criteria.where("uniqueId").regex(search, "i"));

            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }
        query.addCriteria(new Criteria("enabled").is(true));
        long total = mongoTemplate.count(query, Role.class);
        query.with(pageable);
        List<Role> roles = mongoTemplate.find(query, Role.class);
        return new Page<Role>(roles, total, pageable.getPageNumber(), pageable.getPageSize());
    }

}

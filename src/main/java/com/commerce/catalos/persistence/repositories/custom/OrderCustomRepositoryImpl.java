package com.commerce.catalos.persistence.repositories.custom;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Order;
import com.commerce.catalos.persistence.dtos.Role;

@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<Order> searchOrders(final String search, final String channel, final Pageable pageable) {
        Query query = new Query();

        if (search != null && !search.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<Criteria>();

            if (ObjectId.isValid(search)) {
                criteriaList.add(Criteria.where("id").is(new ObjectId(search)));
            }

            criteriaList.add(Criteria.where("email").regex(search, "i"));
            criteriaList.add(Criteria.where("createdBy").regex(search, "i"));
            criteriaList.add(Criteria.where("uniqueId").regex(search, "i"));

            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }
        query.addCriteria(new Criteria("enabled").is(true));
        if (null != channel && !channel.isBlank()) {
            query.addCriteria(new Criteria("channelId").is(channel));
        }
        long total = mongoTemplate.count(query, Role.class);
        query.with(pageable);
        List<Order> roles = mongoTemplate.find(query, Order.class);
        return new Page<Order>(roles, total, pageable.getPageNumber(), pageable.getPageSize());
    }
}

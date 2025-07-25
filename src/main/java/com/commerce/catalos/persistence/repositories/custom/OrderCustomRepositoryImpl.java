package com.commerce.catalos.persistence.repositories.custom;

import com.commerce.catalos.core.utils.TimeUtils;
import com.commerce.catalos.models.orders.OrderFilterInputs;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Order;
import org.springframework.util.CollectionUtils;

@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void populateAdvanceSearchParams(final Query query, final OrderFilterInputs orderFilterInputs) {
        if (orderFilterInputs.getFromDate() != null || orderFilterInputs.getToDate() != null) {
            Criteria createdAtCriteria = Criteria.where("createdAt");

            if (orderFilterInputs.getFromDate() != null) {
                Date start = TimeUtils.parseToStartOfDay(orderFilterInputs.getFromDate());
                createdAtCriteria = createdAtCriteria.gte(start);
            }

            if (orderFilterInputs.getToDate() != null) {
                Date end = TimeUtils.parseToEndOfDay(orderFilterInputs.getToDate());
                createdAtCriteria = createdAtCriteria.lte(end);
            }

            query.addCriteria(createdAtCriteria);
        }

        if (null != orderFilterInputs.getStatuses() && !CollectionUtils.isEmpty(orderFilterInputs.getStatuses())) {
            if (orderFilterInputs.isExcludeStatuses()) {
                query.addCriteria(Criteria.where("status").nin(orderFilterInputs.getStatuses()));
            } else {
                query.addCriteria(Criteria.where("status").in(orderFilterInputs.getStatuses()));
            }
        }
    }

    public Page<Order> searchOrders(
            final String search, final String channel, final OrderFilterInputs orderFilterInputs,
            final Pageable pageable) {
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

        if (null != orderFilterInputs) {
            populateAdvanceSearchParams(query, orderFilterInputs);
        }

        query.addCriteria(new Criteria("enabled").is(true));
        if (null != channel && !channel.isBlank()) {
            query.addCriteria(new Criteria("channelId").is(channel));
        }
        long total = mongoTemplate.count(query, Order.class);
        query.with(pageable);
        List<Order> roles = mongoTemplate.find(query, Order.class);
        return new Page<Order>(roles, total, pageable.getPageNumber(), pageable.getPageSize());
    }
}

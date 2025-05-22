package com.commerce.catalos.persistances.repositories.custom;

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
import com.commerce.catalos.persistances.dtos.User;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Retrieves a page of users based on the given search query. The query is
     * searched against the {@code id}, {@code email}, and {@code firstName}
     * fields of the user document.
     * 
     * @param search   the search query
     * @param pageable the pagination details
     * @return a page of users
     */
    @Override
    public Page<User> searchUsers(String search, Pageable pageable) {
        Query query = new Query();

        if (search != null && !search.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<>();

            if (ObjectId.isValid(search)) {
                criteriaList.add(Criteria.where("id").is(new ObjectId(search)));
            }

            criteriaList.add(Criteria.where("email").regex(search, "i"));
            criteriaList.add(Criteria.where("firstName").regex(search, "i"));

            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, User.class);
        query.with(pageable);
        List<User> users = mongoTemplate.find(query, User.class);
        return new Page<User>(users, total, pageable.getPageNumber(), pageable.getPageSize());
    }
}

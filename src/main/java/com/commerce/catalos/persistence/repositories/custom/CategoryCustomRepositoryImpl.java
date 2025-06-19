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
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.persistence.dtos.Category;

@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Category> searchCategories(final String search, final String parent, final Pageable pageable) {
        Query query = new Query();
        if (search != null && !search.isEmpty()) {
            List<Criteria> criteriaList = new ArrayList<Criteria>();

            if (ObjectId.isValid(search)) {
                criteriaList.add(Criteria.where("id").is(new ObjectId(search)));
            }

            criteriaList.add(Criteria.where("name").regex(search, "i"));
            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }

        if (!parent.equals(SortConstants.ALL)) {
            query.addCriteria(new Criteria("parentId").is(parent.equals(SortConstants.ROOT) ? null : parent));
        }

        query.addCriteria(new Criteria("enabled").is(true));
        long total = mongoTemplate.count(query, Category.class);
        query.with(pageable);
        List<Category> categories = mongoTemplate.find(query, Category.class);
        return new Page<Category>(categories, total, pageable.getPageNumber(), pageable.getPageSize());
    }

}

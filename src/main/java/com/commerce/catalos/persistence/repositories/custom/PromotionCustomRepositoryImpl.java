package com.commerce.catalos.persistence.repositories.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.commerce.catalos.persistence.dtos.Discount;

@Repository
public class PromotionCustomRepositoryImpl implements PromotionCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Discount> getActiveDiscounts(
            final String variantId,
            final String productId,
            final String channelId,
            final Integer quantity,
            final String customerGroupId,
            final String categoryId,
            final String brandId,
            final String collectionId) {

        Date now = new Date();
        Criteria criteria = new Criteria();

        criteria.and("minItemQuantity").gte(quantity);
        criteria.and("startDate").lte(now);
        criteria.and("expireDate").gte(now);
        criteria.and("availableChannel").is(channelId);
        criteria.and("enabled").is(true);
        criteria.and("active").is(true);

        List<Criteria> orConditions = new ArrayList<>();

        if (productId != null && !productId.isBlank()) {
            orConditions.add(Criteria.where("targetedProductIds").in(productId));
        }

        if (variantId != null && !variantId.isBlank()) {
            orConditions.add(Criteria.where("targetedVariantIds").in(variantId));
        }

        if (categoryId != null && !categoryId.isBlank()) {
            orConditions.add(Criteria.where("targetedCategories").in(categoryId));
        }

        if (brandId != null && !brandId.isBlank()) {
            orConditions.add(Criteria.where("targetedBrands").in(brandId));
        }

        if (collectionId != null && !collectionId.isBlank()) {
            orConditions.add(Criteria.where("targetedCollections").in(collectionId));
        }

        orConditions.add(Criteria.where("forAllProducts").in(true));

        // List<Criteria> userGroupCriteria = new ArrayList<>();
        // userGroupCriteria.add(Criteria.where("targetedUserGroup").is(null));
        // if (!customerGroupId.isBlank()) {
        // userGroupCriteria.add(Criteria.where("targetedUserGroup").is(customerGroupId));
        // }

        criteria.andOperator(new Criteria().orOperator(orConditions.toArray(new Criteria[0])));

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Discount.class);
    }

}

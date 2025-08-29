package com.commerce.catalos.persistence.repositories.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.promotions.PromotionFilterInputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

        criteria.and("minItemQuantity").lte(quantity);
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

    @Override
    public Page<Discount> getPromotions(final String search, final String channel,
                                        final PromotionFilterInputs promotionFilterInputs, final Pageable pageable) {
        Criteria criteria = new Criteria();

        if (null != promotionFilterInputs.getStartDate()) {
            criteria.and("startDate").lte(promotionFilterInputs.getStartDate());
        }

        if (null != promotionFilterInputs.getExpireDate()) {
            criteria.and("expireDate").gte(promotionFilterInputs.getExpireDate());
        }

        if (null != promotionFilterInputs.getDiscountType()) {
            criteria.and("discountType").is(promotionFilterInputs.getDiscountType());
        }

        if (null != promotionFilterInputs.getDiscountMode()) {
            criteria.and("discountMode").is(promotionFilterInputs.getDiscountMode());
        }

        if (null != promotionFilterInputs.getTargetedUserGroup()) {
            criteria.and("targetedUserGroup").is(promotionFilterInputs.getTargetedUserGroup());
        }

        List<Criteria> orConditions = new ArrayList<>();

        if (null != search && !search.isBlank()){
            orConditions.add(Criteria.where("targetedProductIds").in(search));
            orConditions.add(Criteria.where("name").regex(search, "i"));
            if (search.startsWith("v:")){
                orConditions.add(Criteria.where("targetedVariantIds").in(search));
            }
            if (search.startsWith("b:")){
                orConditions.add(Criteria.where("targetedBrands").in(search));
            }
            if (search.startsWith("c:")){
                orConditions.add(Criteria.where("targetedCategories").in(search));
                orConditions.add(Criteria.where("targetedCollections").in(search));
            }
        }

        criteria.and("forAllProducts").is(promotionFilterInputs.isForAllProducts());
        criteria.andOperator(new Criteria().orOperator(orConditions.toArray(new Criteria[0])));

        Query query = new Query(criteria);
        query.addCriteria(new Criteria("enabled").is(true));

        long total = mongoTemplate.count(query, Discount.class);
        query.with(pageable);
        List<Discount> promotions = mongoTemplate.find(query, Discount.class);
        return new Page<Discount>(promotions, total, pageable.getPageNumber(), pageable.getPageSize());
    }

}

package com.commerce.catalos.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.commerce.catalos.persistence.dtos.Variant;
import com.commerce.catalos.persistence.repositories.custom.VariantCustomRepository;

import java.util.List;
import java.util.Set;

public interface VariantRepository extends MongoRepository<Variant, String>, VariantCustomRepository {

    boolean existsBySkuIdOrSlugAndEnabled(final String sku, final String slug, final boolean enabled);

    Variant findVariantByIdAndEnabled(final String id, final boolean enabled);

    Variant findVariantBySkuIdAndEnabled(String skuId, boolean enabled);

    Variant findVariantByUrlAndEnabled(String url, boolean enabled);

    List<Variant> findVariantByProductIdAndEnabled(final String productId, final boolean enabled);

    Set<String> findProductIdsOfVariants(final List<String> variantIds);
}

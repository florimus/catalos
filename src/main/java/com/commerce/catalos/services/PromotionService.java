package com.commerce.catalos.services;

import com.commerce.catalos.models.promotions.CreatePromotionRequest;
import com.commerce.catalos.models.promotions.CreatePromotionResponse;

public interface PromotionService {

    CreatePromotionResponse createPromotion(final CreatePromotionRequest createPromotionRequest);

}

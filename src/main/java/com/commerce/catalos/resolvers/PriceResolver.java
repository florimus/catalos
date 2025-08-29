package com.commerce.catalos.resolvers;

import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PriceResolver {

    private final PriceService priceService;

    @QueryMapping()
    public CalculatedPriceResponse getPriceOfSku(
            @Argument("id") final String id, @Argument("channel") final String channel, @Argument("quantity") final Integer quantity) {
        return this.priceService.getPriceOfSku(id, channel, quantity);
    }
}

package com.commerce.catalos.services;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.OrderStatus;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.OrderHelper;
import com.commerce.catalos.models.orders.CreateOrderRequest;
import com.commerce.catalos.models.orders.DeleteOrderLineItemRequest;
import com.commerce.catalos.models.orders.LineItem;
import com.commerce.catalos.models.orders.LineItemError;
import com.commerce.catalos.models.orders.LineItemPrice;
import com.commerce.catalos.models.orders.OrderPrice;
import com.commerce.catalos.models.orders.OrderRequestLineItem;
import com.commerce.catalos.models.orders.OrderResponse;
import com.commerce.catalos.models.orders.UpdateOrderLineItemRequest;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.stocks.StockInfo;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.persistence.dtos.Order;
import com.commerce.catalos.persistence.repositories.OrderRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final AuthContext authContext;

    @Lazy
    @Autowired
    private VariantService variantService;

    @Lazy
    @Autowired
    private ProductService productService;

    @Lazy
    @Autowired
    private PriceService priceService;

    @Lazy
    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private StockService stockService;

    private Order findRunningOrderByUserIdAndChannelId(final String userId, final String channelId) {
        return this.orderRepository.findOrderByUserIdAndChannelIdAndStatusAndActiveAndEnabled(
                userId, channelId, OrderStatus.InProgress, true, true);
    }

    private Order findOrderById(final String orderId) {
        return this.orderRepository.findOrderByIdAndEnabled(orderId, true);
    }

    private Order initializeNewOrder(String userId, String channelId) {
        Logger.info("aba1efef-fb8b-4ac9-9359-d61a44129852", "Creating new order for user: {} and channel: {}", userId,
                channelId);
        Order order = new Order();
        order.setUserId(userId);
        order.setChannelId(channelId);
        order.setStatus(OrderStatus.InProgress);
        order.setActive(true);
        order.setEnabled(true);
        order.setCreatedAt(new Date());

        GetUserInfoResponse user = authContext.getCurrentUser();
        if (user != null) {
            order.setUpdatedBy(user.getId());
        }

        return order;
    }

    private Map<String, Integer> prepareVariantQuantityMap(Order order, List<OrderRequestLineItem> lineItems) {
        Map<String, Integer> variantQuantityMap = new LinkedHashMap<>();

        if (order.getLineItems() != null) {
            for (LineItem item : order.getLineItems()) {
                variantQuantityMap.put(item.getVariant().getId(), item.getQuantity());
            }
        }

        List<OrderRequestLineItem> requestItems = Optional.ofNullable(lineItems)
                .orElse(Collections.emptyList());
        for (OrderRequestLineItem requestItem : requestItems) {
            variantQuantityMap.put(requestItem.getVariantId(), requestItem.getQuantity());
        }

        return variantQuantityMap;
    }

    private List<LineItem> buildLineItems(Map<String, Integer> variantQuantityMap, String channelId) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<LineItem>> futures = variantQuantityMap.entrySet().stream()
                .map(entry -> CompletableFuture.supplyAsync(() -> {
                    String variantId = entry.getKey();
                    int quantity = entry.getValue();

                    VariantResponse variant = variantService.getVariantById(variantId);
                    ProductResponse product = productService.getProductById(variant.getProductId());
                    StockInfo stock = stockService.getStockInfoByVariantIdAndChannel(variantId, channelId);
                    CalculatedPriceResponse prices = priceService.getPriceOfSku(variant.getSkuId(), channelId,
                            quantity);

                    LineItem lineItem = new LineItem();
                    lineItem.setId(variantId);
                    lineItem.setVariant(variant);
                    lineItem.setProduct(product);
                    lineItem.setQuantity(quantity);

                    if (null != stock) {
                        Integer availableStocks = stock.getTotalStocks() - stock.getReservedStocks()
                                - stock.getSafetyStocks();

                        if (quantity > availableStocks) {
                            lineItem.setError(new LineItemError(variantId, "Insufficient stock"));
                        }
                    }

                    if (prices != null) {
                        LineItemPrice lineItemPrice = OrderHelper.toLineItemPriceFromCalculatedPriceResponse(prices);
                        lineItem.setItemPrice(lineItemPrice);
                    }

                    return lineItem;
                }, executor)).collect(Collectors.toList());

        List<LineItem> finalLineItems = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        executor.shutdown();
        return finalLineItems;
    }

    private OrderPrice calculateOrderPrice(List<LineItem> lineItems) {
        float subtotal = 0f, tax = 0f, discount = 0f;

        for (LineItem item : lineItems) {
            LineItemPrice price = item.getItemPrice();
            if (price != null) {
                subtotal += price.getSalesPrice();
                tax += price.getTaxPrice();
                discount += price.getDiscountFlatPrice();
            }
        }

        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setSubtotalPrice(subtotal);
        orderPrice.setTotalTaxPrice(tax);
        orderPrice.setTotalDiscountPrice(discount);
        orderPrice.setShippingPrice(0f);
        orderPrice.setGrandTotalPrice(subtotal + tax - discount);

        return orderPrice;
    }

    @Override
    public OrderResponse createOrder(final CreateOrderRequest request) {
        String userId = request.getUserId();
        String channelId = request.getChannelId();

        Order order = null;

        if (null == userId || userId.isBlank()) {
            userId = java.util.UUID.randomUUID().toString();
        } else {
            order = findRunningOrderByUserIdAndChannelId(userId, channelId);
            try {
                GetUserInfoResponse userInfo = userService.getUserInfoById(userId);
                if (null != userInfo && null != order) {
                    order.setUserId(userId);
                    order.setEmail(userInfo.getEmail());
                }
            } catch (Exception e) {
                Logger.info("5218203d-d7d1-4a55-934d-1a7a66d18b7b", "Creating order for guest user: {}",
                        userId);
            }
        }

        if (order == null) {
            order = initializeNewOrder(userId, channelId);
        }

        Map<String, Integer> variantQuantityMap = prepareVariantQuantityMap(order, request.getLineItems());

        List<LineItem> finalLineItems = buildLineItems(variantQuantityMap, channelId);
        order.setLineItems(finalLineItems);

        if (!finalLineItems.isEmpty()) {
            order.setPrice(calculateOrderPrice(finalLineItems));
        }

        GetUserInfoResponse user = authContext.getCurrentUser();
        if (user != null) {
            order.setUpdatedBy(user.getId());
        }

        order.setUpdatedAt(new Date());
        orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse updateOrderLineItems(String orderId, UpdateOrderLineItemRequest updateOrderLineItemRequest) {
        Order order = this.findOrderById(orderId);
        if (null == order) {
            Logger.error("bf157555-c712-4bc6-8609-986e6dbeaa45", "Order not found for order id: {}", orderId);
            throw new NotFoundException("Order not available");
        }
        Map<String, Integer> variantQuantityMap = prepareVariantQuantityMap(order,
                updateOrderLineItemRequest.getLineItems());

        List<LineItem> finalLineItems = buildLineItems(variantQuantityMap, order.getChannelId());
        order.setLineItems(finalLineItems);

        if (!finalLineItems.isEmpty()) {
            order.setPrice(calculateOrderPrice(finalLineItems));
        }

        GetUserInfoResponse user = authContext.getCurrentUser();
        if (user != null) {
            order.setUpdatedBy(user.getId());
        }

        order.setUpdatedAt(new Date());
        orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse deleteOrderLineItems(String orderId, DeleteOrderLineItemRequest deleteOrderLineItemRequest) {
        Order order = this.findOrderById(orderId);
        if (null == order) {
            Logger.error("29c85472-7d42-4f43-8d78-bf05b3a54c38", "Order not found for order id: {}", orderId);
            throw new NotFoundException("Order not available");
        }
        order.getLineItems().removeIf(
                lineItem -> deleteOrderLineItemRequest.getLineItems().contains(lineItem.getId()));

        Map<String, Integer> variantQuantityMap = prepareVariantQuantityMap(order, List.of());

        List<LineItem> finalLineItems = buildLineItems(variantQuantityMap, order.getChannelId());
        order.setLineItems(finalLineItems);

        if (null != finalLineItems) {
            order.setPrice(calculateOrderPrice(finalLineItems));
        }

        GetUserInfoResponse user = authContext.getCurrentUser();
        if (user != null) {
            order.setUpdatedBy(user.getId());
        }

        order.setUpdatedAt(new Date());
        orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

}

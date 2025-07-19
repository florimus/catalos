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

import com.commerce.catalos.core.configurations.Messager;
import com.commerce.catalos.core.enums.PaymentStatus;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.models.customApps.PaymentDetails;
import com.commerce.catalos.models.customApps.PaymentLinkGeneratedResponse;
import com.commerce.catalos.models.customApps.VerifyPaymentRequest;
import com.commerce.catalos.models.orders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.enums.AddressType;
import com.commerce.catalos.core.enums.OrderStatus;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.OrderHelper;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.stocks.StockInfo;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.persistence.dtos.Order;
import com.commerce.catalos.persistence.dtos.PaymentOption;
import com.commerce.catalos.persistence.repositories.OrderRepository;
import com.commerce.catalos.persistence.repositories.PaymentOptionRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final AuthContext authContext;

    private final PaymentOptionRepository paymentOptionRepository;

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

    @Lazy
    @Autowired
    private CustomPaymentAppService customPaymentAppService;

    @Lazy
    @Autowired
    private Messager messager;

    private PaymentOption findPaymentOptionByIdAndChannel(final String id, final String channel) {
        return this.paymentOptionRepository.findPaymentOptionByIdAndApplicableChannelsInAndEnabledAndActive(id, channel,
                true, true);
    }

    private List<PaymentOption> findOrderPaymentOptions(final String channelId) {
        return this.paymentOptionRepository.findPaymentOptionByApplicableChannelsInAndEnabledAndActive(channelId, true,
                true);
    }

    private Order findRunningOrderByUserIdAndChannelId(final String userId, final String channelId) {
        return this.orderRepository.findOrderByUserIdAndChannelIdAndStatusAndActiveAndEnabled(
                userId, channelId, OrderStatus.InProgress, true, true);
    }

    private Order findRunningOrderByEmailAndChannelId(final String email, final String channelId) {
        return this.orderRepository.findOrderByEmailAndChannelIdAndStatusAndActiveAndEnabled(
                email, channelId, OrderStatus.InProgress, true, true);
    }

    private Order findProgressingOrderById(final String orderId) {
        return this.orderRepository.findOrderByIdAndStatusAndEnabled(orderId, OrderStatus.InProgress, true);
    }

    private Order findOrderById(final String orderId) {
        return this.orderRepository.findOrderByIdAndEnabled(orderId, true);
    }

    private Order initializeNewOrder(String userId, String channelId, String email) {
        Logger.info("aba1efef-fb8b-4ac9-9359-d61a44129852", "Creating new order for user: {} and channel: {}", userId,
                channelId);
        Order order = new Order();
        order.setUserId(userId);
        order.setEmail(email);
        order.setChannelId(channelId);
        order.setStatus(OrderStatus.InProgress);
        order.setActive(true);
        order.setEnabled(true);
        order.setCreatedAt(new Date());

        GetUserInfoResponse user = authContext.getCurrentUser();
        if (user != null) {
            order.setCreatedBy(user.getEmail());
            order.setUpdatedBy(user.getEmail());
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
            order = initializeNewOrder(userId, channelId, null);
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

        order.setPaymentOptions(findOrderPaymentOptions(channelId));

        order.setUpdatedAt(new Date());
        orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse updateOrderLineItems(String orderId, UpdateOrderLineItemRequest updateOrderLineItemRequest) {
        Order order = this.findProgressingOrderById(orderId);
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

        order.setPaymentOptions(findOrderPaymentOptions(order.getChannelId()));

        order.setUpdatedAt(new Date());
        orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse deleteOrderLineItems(String orderId, DeleteOrderLineItemRequest deleteOrderLineItemRequest) {
        Order order = this.findProgressingOrderById(orderId);
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

        order.setPaymentOptions(findOrderPaymentOptions(order.getChannelId()));

        order.setUpdatedAt(new Date());
        orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse getOrderById(final String orderId) {
        Order order = this.findOrderById(orderId);
        if (null == order) {
            Logger.error("91020196-1b09-4fbb-a2bf-028132bb7e79", "Order not found for order id: {}", orderId);
            throw new NotFoundException("Order not available");
        }

        if (order.getStatus().equals(OrderStatus.InProgress)) {
            Map<String, Integer> variantQuantityMap = prepareVariantQuantityMap(order, List.of());

            List<LineItem> finalLineItems = buildLineItems(variantQuantityMap, order.getChannelId());
            order.setLineItems(finalLineItems);

            if (null != finalLineItems) {
                order.setPrice(calculateOrderPrice(finalLineItems));
            }

            order.setPaymentOptions(findOrderPaymentOptions(order.getChannelId()));
        }
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse createOrderByAdmin(final CreateOrderRequest request) {
        String userId = request.getUserId();
        String channelId = request.getChannelId();

        Order order = null;

        if (null == userId || userId.isBlank()) {
            userId = java.util.UUID.randomUUID().toString();
        } else {
            order = findRunningOrderByEmailAndChannelId(userId, channelId);
            try {
                GetUserInfoResponse userInfo = userService.getUserInfoByEmail(userId);
                if (null != userInfo && null != order) {
                    order.setEmail(userInfo.getEmail());
                    order.setUserId(userInfo.getId());
                }
            } catch (Exception e) {
                Logger.info("49ddb14d-094e-4ebd-a610-319485c0e4e2", "Creating order for guest user: {}",
                        userId);
            }
        }

        if (order == null) {
            order = initializeNewOrder(java.util.UUID.randomUUID().toString(), channelId, userId);
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

        order.setPaymentOptions(findOrderPaymentOptions(order.getChannelId()));

        order.setUpdatedAt(new Date());
        order = orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public Page<MiniOrderResponse> getOrders(final String query, final String channel, final Pageable pageable) {
        Logger.info("95b02037-263c-4cba-a200-cf3ba73f2b04", "Finding orders with query: {} and pageable: {}",
                query, pageable);
        Page<Order> orders = orderRepository.searchOrders(query, channel, pageable);
        return new Page<MiniOrderResponse>(
                OrderHelper.toMiniOrderResponseFromOrders(orders.getHits()),
                orders.getTotalHitsCount(),
                orders.getCurrentPage(),
                orders.getPageSize());
    }

    @Override
    public OrderResponse updateAddress(final String orderId, final UpdateAddressRequest updateAddressRequest) {
        if (null == orderId || orderId.isBlank()) {
            Logger.error("d193058b-5b69-4c7d-9cb3-4ad07195e077", "Order id is empty");
            throw new BadRequestException("Order id is empty");
        }

        Order order = findProgressingOrderById(orderId);

        if (null == order) {
            Logger.error("4769f1e0-ff1d-451f-be60-26c5047614c9", "Order not found for order id: {}", orderId);
            throw new NotFoundException("Order not available");
        }

        if (updateAddressRequest.getAddressType().equals(AddressType.Shipping)) {
            Logger.info("bd9352de-8f63-44a5-b4b4-7dd48514306e", "Updating shipping address in order: {}", orderId);
            order.setShippingAddress(OrderHelper.toAddressFromAddressResponse(updateAddressRequest));
        }

        if (updateAddressRequest.getAddressType().equals(AddressType.Billing)) {
            Logger.info("0c898cd5-0dad-49bb-b318-51ad5754635d", "Updating billing address in order: {}", orderId);
            order.setBillingAddress(OrderHelper.toAddressFromAddressResponse(updateAddressRequest));
        }

        order.setPaymentOptions(findOrderPaymentOptions(order.getChannelId()));

        Logger.info("d0365340-9093-4c2e-ab4b-b3f599f17da0", "saving order: {}", orderId);
        order = orderRepository.save(order);
        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse selectPaymentMethod(final String orderId, final String optionId) {
        if (orderId == null || orderId.isBlank()) {
            Logger.error("8684060f-ac04-4bfc-9190-c552605ece84", "Order id is empty");
            throw new BadRequestException("Order id is empty");
        }

        Order order = findProgressingOrderById(orderId);
        if (order == null) {
            Logger.error("807dd27b-ebeb-413c-b915-7a536bedef85", "Order not found for order id: {}", orderId);
            throw new NotFoundException("Order not available");
        }

        if (null == order.getBillingAddress() || null == order.getShippingAddress()) {
            Logger.error("6af75622-0f13-450b-b2f0-848027c4d358", "Addresses are mandatory");
            throw new ConflictException("Address should be filled in order");
        }

        PaymentOption option = findPaymentOptionByIdAndChannel(optionId, order.getChannelId());
        if (option == null) {
            Logger.error("beecf987-dec0-4ecb-8446-6df1b993cb5f", "Payment option not available now");
            throw new ConflictException("Payment option not available now, please select a new payment option");
        }

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setMode(option);
        paymentInfo.setAmount(order.getPrice().getGrandTotalPrice());
        paymentInfo.setStatus(PaymentStatus.Pending);

        if (option.isExternal()) {
            PaymentLinkGeneratedResponse paymentLinkResponse = customPaymentAppService.generatePaymentLink(order,
                    optionId);

            if (paymentLinkResponse != null) {
                paymentInfo.setUniqueId(paymentLinkResponse.getId());
                order.setPaymentInfo(paymentInfo);
                order = orderRepository.save(order);

                OrderResponse response = OrderHelper.toOrderResponseFromOrder(order);
                response.setPaymentLink(paymentLinkResponse.getPaymentUrl());
                return response;
            } else {
                order.setPaymentInfo(null);
                order = orderRepository.save(order);
                return OrderHelper.toOrderResponseFromOrder(order);
            }
        }

        // For non-external payment methods
        paymentInfo.setUniqueId(String.valueOf(System.currentTimeMillis()));
        order.setPaymentInfo(paymentInfo);

        Logger.info("258893e2-8674-41a5-80d0-4e9df5523883", "Saving order: {}", orderId);
        order = orderRepository.save(order);

        return OrderHelper.toOrderResponseFromOrder(order);
    }

    @Override
    public OrderResponse updateOrderTransaction(final String orderId,
            final OrderTransactionRequest orderTransactionRequest) {
        if (orderId == null || orderId.isBlank()) {
            Logger.error("4530494f-174c-4c75-b103-882fad78e228", "Order id is empty");
            throw new BadRequestException("Order id is empty");
        }

        Order order = findProgressingOrderById(orderId);
        if (order == null) {
            Logger.error("b7faefaf-5a06-4f4a-abd8-525f8a81a331", "Order not found for order id: {}", orderId);
            throw new NotFoundException("Order not available");
        }

        PaymentInfo paymentInfo = order.getPaymentInfo();
        if (paymentInfo.getUniqueId().equals(orderTransactionRequest.getPaymentUniqueId())) {

            VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
            verifyPaymentRequest.setPaymentLink(paymentInfo.getUniqueId());
            verifyPaymentRequest.setAmount(order.getPrice().getGrandTotalPrice());
            verifyPaymentRequest.setPaymentId(orderTransactionRequest.getPaymentId());

            PaymentDetails paymentDetails = this.customPaymentAppService.verifyPayment(paymentInfo.getMode().getId(),
                    verifyPaymentRequest);
            if (null != paymentDetails) {
                paymentInfo.setPaymentId(paymentDetails.getPayment_id());
                paymentInfo.setPaymentAt(paymentDetails.getCreated_at());
                paymentInfo.setMethod(paymentDetails.getMethod());
                paymentInfo.setStatus(PaymentStatus.Confirmed);
                order.setPaymentInfo(paymentInfo);
                order.setStatus(OrderStatus.Submitted);
                Logger.info("b2d27177-bc03-4677-a72e-71b3ad49ef1d", "Saving order: {}", orderId);
                order = orderRepository.save(order);
                messager.send("order/"+ orderId, Map.of("success", true));
                return OrderHelper.toOrderResponseFromOrder(order);
            }
        }
        Logger.error("e28d054e-65e1-49e0-9bf9-e8a0190c141a", "Payment unique id not matching");
        throw new ConflictException("Invalid transaction");
    }
}

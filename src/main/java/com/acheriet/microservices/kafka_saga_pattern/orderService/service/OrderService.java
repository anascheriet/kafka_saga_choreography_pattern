package com.acheriet.microservices.kafka_saga_pattern.orderService.service;

import com.acheriet.microservices.kafka_saga_pattern.models.dto.OrderRequestDto;
import com.acheriet.microservices.kafka_saga_pattern.models.enums.OrderStatus;
import com.acheriet.microservices.kafka_saga_pattern.orderService.entity.PurchaseOrder;
import com.acheriet.microservices.kafka_saga_pattern.orderService.eventHandlers.OrderEventPublisherService;
import com.acheriet.microservices.kafka_saga_pattern.orderService.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private PurchaseOrderRepository repository;

    @Autowired
    private OrderEventPublisherService publisherService;

    private static final HashMap<Integer, Integer> PRODUCT_PRICE = new HashMap<>();
    static {
        PRODUCT_PRICE.put(1, 100);
        PRODUCT_PRICE.put(2, 200);
        PRODUCT_PRICE.put(3, 300);
    }

    public PurchaseOrder createOrder(OrderRequestDto requestDto) {
        PurchaseOrder purchaseOrder = repository.save(dtoToEntity(requestDto));
        publisherService.raiseOrderCreatedEvent(purchaseOrder);
        return purchaseOrder;
    }

    public List<PurchaseOrder> getAll() {
        return repository.findAll();
    }

    private PurchaseOrder dtoToEntity(OrderRequestDto dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(PRODUCT_PRICE.get(purchaseOrder.getProductId()));
        return purchaseOrder;
    }

}

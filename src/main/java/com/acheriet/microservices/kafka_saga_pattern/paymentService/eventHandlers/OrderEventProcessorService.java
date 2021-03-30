package com.acheriet.microservices.kafka_saga_pattern.paymentService.eventHandlers;

import com.acheriet.microservices.kafka_saga_pattern.models.enums.OrderStatus;
import com.acheriet.microservices.kafka_saga_pattern.models.enums.PaymentStatus;
import com.acheriet.microservices.kafka_saga_pattern.models.events.OrderEvent;
import com.acheriet.microservices.kafka_saga_pattern.models.events.PaymentEvent;
import com.acheriet.microservices.kafka_saga_pattern.orderService.repository.PurchaseOrderRepository;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderEventProcessorService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    // user - credit limit
    public static final Map<Integer, Integer> userMap = new HashMap<>();
    static {
        userMap.put(1, 1000);
        userMap.put(2, 1000);
        userMap.put(3, 1000);
        userMap.put(4, 1000);
        userMap.put(5, 1000);
    }

    public PaymentEvent processOrderEvent(OrderEvent orderEvent) {
        var price = orderEvent.getPrice();
        var creditLimit = userMap.get(orderEvent.getUserId());
        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(orderEvent.getOrderId());
        if (creditLimit >= price) {
            paymentEvent.setStatus(PaymentStatus.APPROVED);
            userMap.computeIfPresent(orderEvent.getUserId(), (k, v) -> v - price);
            // purchaseOrderRepository.findById(orderEvent.getOrderId()).get().setStatus(OrderStatus.ORDER_COMPLETED);
        }
        else {
            paymentEvent.setStatus(PaymentStatus.REJECTED);
            // purchaseOrderRepository.findById(orderEvent.getOrderId()).get().setStatus(OrderStatus.ORDER_CANCELLED);
        }
        return paymentEvent;
    }

    public OrderEvent consumerOutputToOrderEvent(String message) {
        Map<String, Integer> order = new HashMap<>();
        var arr = message.split("\\{")[1];
        arr = arr.split("}")[0];

        var tab = arr.split(",");

        var orderEvent = new OrderEvent();
        orderEvent.setOrderId(Integer.parseInt(tab[0].split("=")[1]));
        orderEvent.setPrice(Integer.parseInt(tab[2].split("=")[1]));
        orderEvent.setUserId(Integer.parseInt(tab[1].split("=")[1]));

        for (String x : tab) {
            order.put(x.split("=")[0].replace(" ", ""), Integer.parseInt(x.split("=")[1]));
        }
        return orderEvent;
    }

    @KafkaListener(topics = "saga-topic", groupId = "saga_app")
    public void consumeOrderEvents(String message) {
        Logger logger = LoggerFactory.getLogger(OrderEventProcessorService.class);
        logger.info(String.format("#### -> Consumed Event -> %s", message));
        System.out.println(processOrderEvent(consumerOutputToOrderEvent(message)));
    }

}

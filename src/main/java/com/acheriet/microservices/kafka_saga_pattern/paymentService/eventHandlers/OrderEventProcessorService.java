package com.acheriet.microservices.kafka_saga_pattern.paymentService.eventHandlers;

import com.acheriet.microservices.kafka_saga_pattern.models.enums.PaymentStatus;
import com.acheriet.microservices.kafka_saga_pattern.models.events.OrderEvent;
import com.acheriet.microservices.kafka_saga_pattern.models.events.PaymentEvent;
import lombok.var;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class OrderEventProcessorService {

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
        }
        else {
            paymentEvent.setStatus(PaymentStatus.REJECTED);
        }
        return paymentEvent;
    }

    @KafkaListener(topics = "saga-topic", groupId = "saga_app")
    public void consumeOrderEvents(String message) {
        Logger logger = LoggerFactory.getLogger(OrderEventProcessorService.class);
        logger.info(String.format("#### -> Consumed Event -> %s", message));
    }

}

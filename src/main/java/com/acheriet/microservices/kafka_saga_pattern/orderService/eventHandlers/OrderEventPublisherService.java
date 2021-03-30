package com.acheriet.microservices.kafka_saga_pattern.orderService.eventHandlers;

import com.acheriet.microservices.kafka_saga_pattern.models.events.OrderEvent;
import com.acheriet.microservices.kafka_saga_pattern.orderService.entity.PurchaseOrder;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class OrderEventPublisherService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    String topic = "saga-topic";

    public void raiseOrderCreatedEvent(final PurchaseOrder purchaseOrder) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(purchaseOrder.getId());
        orderEvent.setUserId(purchaseOrder.getUserId());
        orderEvent.setPrice(purchaseOrder.getPrice());

        publishOrderEvent(orderEvent.toString());

    }

    public void publishOrderEvent(String order) {
        this.kafkaTemplate.send(topic, order);
    }

}

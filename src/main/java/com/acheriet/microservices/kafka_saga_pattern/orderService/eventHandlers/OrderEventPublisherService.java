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

    //String bootstrapServers = "127.0.0.1:9092";
    //Properties properties = new Properties();
    // create producer
    //KafkaProducer<String, String> producer;

    /*public void initProps() {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // initialize producer
        //producer = new KafkaProducer<String, String>(properties);
    }*/

    public void raiseOrderCreatedEvent(final PurchaseOrder purchaseOrder) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(purchaseOrder.getId());
        orderEvent.setUserId(purchaseOrder.getUserId());
        orderEvent.setPrice(purchaseOrder.getPrice());

        publishMessage(orderEvent.toString());

    }

    public void publishMessage(String message) {
        this.kafkaTemplate.send(topic, message);
    }

}

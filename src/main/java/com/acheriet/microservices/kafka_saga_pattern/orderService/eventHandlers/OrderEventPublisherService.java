package com.acheriet.microservices.kafka_saga_pattern.orderService.eventHandlers;

import com.acheriet.microservices.kafka_saga_pattern.models.events.OrderEvent;
import com.acheriet.microservices.kafka_saga_pattern.orderService.entity.PurchaseOrder;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class OrderEventPublisherService {

    String bootstrapServers = "127.0.0.1:9092";
    Properties properties = new Properties();
    String topic = "saga-topic";
    // create producer
    KafkaProducer<String, String> producer;

    public void initProps() {
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // initialize producer
        producer = new KafkaProducer<String, String>(properties);
    }

    public void raiseOrderCreatedEvent(final PurchaseOrder purchaseOrder) {
        initProps();

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(purchaseOrder.getId());
        orderEvent.setUserId(purchaseOrder.getUserId());
        orderEvent.setPrice(purchaseOrder.getPrice());

        // create the data to send (producer record)
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, orderEvent.toString());
        
        // send data
        producer.send(record);
        producer.flush();
    }

}

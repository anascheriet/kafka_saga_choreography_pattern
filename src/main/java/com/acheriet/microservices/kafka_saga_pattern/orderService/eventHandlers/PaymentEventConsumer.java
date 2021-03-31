package com.acheriet.microservices.kafka_saga_pattern.orderService.eventHandlers;

import com.acheriet.microservices.kafka_saga_pattern.models.enums.OrderStatus;
import com.acheriet.microservices.kafka_saga_pattern.models.enums.PaymentStatus;
import com.acheriet.microservices.kafka_saga_pattern.models.events.PaymentEvent;
import com.acheriet.microservices.kafka_saga_pattern.orderService.repository.PurchaseOrderRepository;
import com.acheriet.microservices.kafka_saga_pattern.paymentService.eventHandlers.OrderEventProcessorService;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentEventConsumer {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    // Receive The payment event and update the purchase order in the Db accordingly
    @KafkaListener(topics = "saga-topic", groupId = "saga_app", topicPartitions = {
            @TopicPartition(topic = "saga-topic", partitions = "2") })
    public void consumeCreatedOrderEvent(String message) {
        Logger logger = LoggerFactory.getLogger(OrderEventProcessorService.class);
        logger.info(String.format("#### -> Consumed Payment Event -> %s", message));
        finalizeOrder(stringToPaymentEvent(message));
    }

    public PaymentEvent stringToPaymentEvent(String message) {
        Map<String, Integer> order = new HashMap<>();
        var arr = message.split("\\(")[1];
        arr = arr.split("\\)")[0];

        var tab = arr.split(",");

        var paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(Integer.parseInt(tab[0].split("=")[1]));
        paymentEvent.setStatus(PaymentStatus.valueOf(tab[1].split("=")[1]));

        return paymentEvent;
    }

    public void finalizeOrder(PaymentEvent paymentEvent) {
        purchaseOrderRepository.findById(paymentEvent.getOrderId()).map(order -> {
            order
                    .setStatus(
                            paymentEvent.getStatus().equals(PaymentStatus.APPROVED) ? OrderStatus.ORDER_COMPLETED
                                    : OrderStatus.ORDER_CANCELLED);
            return purchaseOrderRepository.save(order);
        });
    }
}

package com.acheriet.microservices.kafka_saga_pattern.models.events;

import lombok.Data;

@Data
public class OrderEvent {
    private Integer orderId;
    private Integer userId;
    private Integer price;
}

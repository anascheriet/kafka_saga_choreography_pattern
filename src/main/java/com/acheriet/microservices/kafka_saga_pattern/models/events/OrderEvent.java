package com.acheriet.microservices.kafka_saga_pattern.models.events;

import lombok.Data;
import lombok.ToString;

@Data
public class OrderEvent {
    private Integer orderId;
    private Integer userId;
    private Integer price;


    public String toString() {
        return "OrderEvent{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", price=" + price +
                '}';
    }
}

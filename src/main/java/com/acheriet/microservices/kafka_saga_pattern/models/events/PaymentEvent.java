package com.acheriet.microservices.kafka_saga_pattern.models.events;

import com.acheriet.microservices.kafka_saga_pattern.models.enums.PaymentStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentEvent {
    private Integer orderId;
    private PaymentStatus status;
}

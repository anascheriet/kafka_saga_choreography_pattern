package com.acheriet.microservices.kafka_saga_pattern.orderService.entity;

import com.acheriet.microservices.kafka_saga_pattern.models.enums.OrderStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer price;
    private OrderStatus status;
}

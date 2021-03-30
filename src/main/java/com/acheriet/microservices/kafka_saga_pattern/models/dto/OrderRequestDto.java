package com.acheriet.microservices.kafka_saga_pattern.models.dto;

import lombok.Data;

@Data
public class OrderRequestDto {
    private Integer userId;
    private Integer productId;
}

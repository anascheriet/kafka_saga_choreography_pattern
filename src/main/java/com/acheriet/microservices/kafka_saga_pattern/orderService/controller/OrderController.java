package com.acheriet.microservices.kafka_saga_pattern.orderService.controller;

import com.acheriet.microservices.kafka_saga_pattern.models.dto.OrderRequestDto;
import com.acheriet.microservices.kafka_saga_pattern.orderService.entity.PurchaseOrder;
import com.acheriet.microservices.kafka_saga_pattern.orderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public PurchaseOrder createOrder(@RequestBody OrderRequestDto orderRequestDto)
    {
        return orderService.createOrder(orderRequestDto);
    }

    @GetMapping("")
    public List<PurchaseOrder> getOrders(){
        return orderService.getAll();
    }
}

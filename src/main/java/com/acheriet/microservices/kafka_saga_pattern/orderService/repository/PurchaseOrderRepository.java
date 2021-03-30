package com.acheriet.microservices.kafka_saga_pattern.orderService.repository;

import com.acheriet.microservices.kafka_saga_pattern.orderService.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
}

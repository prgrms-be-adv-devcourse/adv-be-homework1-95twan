package com.rodem.homework.order.infrastructure;

import com.rodem.homework.order.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<PurchaseOrder, UUID> {
}

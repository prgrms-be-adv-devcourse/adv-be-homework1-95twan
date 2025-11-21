package com.rodem.homework.order.application;

import com.rodem.homework.common.ResponseEntity;
import com.rodem.homework.order.application.dto.OrderCommand;
import com.rodem.homework.order.application.dto.OrderInfo;
import com.rodem.homework.order.domain.OrderRepository;
import com.rodem.homework.order.domain.PurchaseOrder;
import com.rodem.homework.order.domain.PurchaseOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public ResponseEntity<List<OrderInfo>> findAll(Pageable pageable) {
        Page<PurchaseOrder> page = orderRepository.findAll(pageable);
        List<OrderInfo> orders = page.stream().map(OrderInfo::from).toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), orders, page.getTotalElements());
    }

    public ResponseEntity<OrderInfo> create(OrderCommand command) {
        PurchaseOrder purchaseOrder = PurchaseOrder.create(
                command.productId(),
                command.sellerId(),
                command.memberId(),
                command.amount()
        );

        PurchaseOrder saved = orderRepository.save(purchaseOrder);
        return new ResponseEntity<>(HttpStatus.CREATED.value(), OrderInfo.from(saved), 1);
    }

    public ResponseEntity<OrderInfo> statusChange(UUID id, PurchaseOrderStatus status) {
        PurchaseOrder order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        order.setStatus(status);
        orderRepository.save(order);
        return new ResponseEntity<>(HttpStatus.OK.value(), OrderInfo.from(order), 1);
    }
}

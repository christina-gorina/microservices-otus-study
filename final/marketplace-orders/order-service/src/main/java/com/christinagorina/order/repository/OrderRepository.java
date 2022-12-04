package com.christinagorina.order.repository;

import com.christinagorina.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderUuid(UUID orderUuid);
}

package com.christinagorina.billing.repository;

import com.christinagorina.billing.model.OrdersIdempotent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersIdempotentRepository extends JpaRepository<OrdersIdempotent, Long> {

    Optional<OrdersIdempotent> findByOrderId(Long id);

}

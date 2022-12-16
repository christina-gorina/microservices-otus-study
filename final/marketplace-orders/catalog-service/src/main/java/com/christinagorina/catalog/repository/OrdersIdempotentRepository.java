package com.christinagorina.catalog.repository;

import com.christinagorina.catalog.model.OrdersIdempotent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersIdempotentRepository extends JpaRepository<OrdersIdempotent, Long> {

    Optional<OrdersIdempotent> findByOrderId(Long id);

}

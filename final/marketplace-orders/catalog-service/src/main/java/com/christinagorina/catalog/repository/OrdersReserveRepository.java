package com.christinagorina.catalog.repository;

import com.christinagorina.catalog.model.OrdersReserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrdersReserveRepository extends JpaRepository<OrdersReserve, Long> {

    Optional<OrdersReserve> findByUuid(UUID uuid);

}

package com.christinagorina.repository;

import com.christinagorina.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderIdentifier(String orderIdentifier);

}

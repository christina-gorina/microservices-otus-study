package com.christinagorina.logistics.repostory;

import com.christinagorina.logistics.model.OrdersIdempotent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrdersoIdempotentRepository extends MongoRepository<OrdersIdempotent, Long> {

    Optional<OrdersIdempotent> findByOrderId(Long id);

}


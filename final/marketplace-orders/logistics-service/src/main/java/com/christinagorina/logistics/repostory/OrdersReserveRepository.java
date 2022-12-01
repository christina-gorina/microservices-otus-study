package com.christinagorina.logistics.repostory;

import com.christinagorina.logistics.model.OrdersReserve;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrdersReserveRepository extends MongoRepository<OrdersReserve, Long> {

    Optional<OrdersReserve> findByUuid(UUID uuid);

}


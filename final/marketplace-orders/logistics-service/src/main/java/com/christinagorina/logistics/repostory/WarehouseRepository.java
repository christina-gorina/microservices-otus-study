package com.christinagorina.logistics.repostory;

import com.christinagorina.logistics.model.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WarehouseRepository extends MongoRepository<Warehouse, String> {

}

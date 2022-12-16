package com.christinagorina.logistics.repostory;

import com.christinagorina.logistics.model.Warehouse;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.geo.Point;


public interface WarehouseRepository extends MongoRepository<Warehouse, String> {

   GeoResults<Warehouse> findByLocationNear(Point location, Distance distance);

}

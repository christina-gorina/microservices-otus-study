package com.christinagorina.logistics.controller;

import com.christinagorina.logistics.model.WarehouseDto;
//TODO исправить
//import com.christinagorina.logistics.repository.WarehouseRepository;
//import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class LogisticsController {
    //TODO исправить
    //private final WarehouseRepository warehouseRepository;
    private final MongoDatabase mongoDatabase;

    @PostMapping("/api/warehouse")
    public String create(@RequestBody WarehouseDto warehouseDto) {
        log.info("warehouseDto qwe = " + warehouseDto);

        MongoCollection<Document> collection = mongoDatabase.getCollection("warehouses");
        collection.createIndex(Indexes.geo2dsphere("location"));
        Point currentLocAnino = new Point(new Position(55.583037, 37.595277));
        //TODO еще один адрес с другого конца
        Point currentLocVarshavskaya = new Point(new Position(55.653307, 37.620642));
        FindIterable<Document> result = collection.find(Filters.near("location", currentLocAnino, 10000.0, 1.0));

        System.out.println("\n\n----------------------------------------------\n\n\n");
        //System.out.println("bligayshaya = " + result.first().get("name"));
        result.forEach(r -> System.out.println(r.get("name")));
        System.out.println("\n\n----------------------------------------------\n\n\n");

        return "WarehouseDto success";
    }

}

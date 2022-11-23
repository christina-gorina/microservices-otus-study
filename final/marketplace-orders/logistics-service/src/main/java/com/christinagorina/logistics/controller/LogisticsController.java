package com.christinagorina.logistics.controller;

import com.christinagorina.logistics.model.Person;
import com.christinagorina.logistics.model.WarehouseDto;
//TODO исправить
//import com.christinagorina.logistics.repository.WarehouseRepository;
import com.christinagorina.logistics.repostory.PersonRepository;
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
    private final PersonRepository personRepository;


    //private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    @PostMapping("/api/warehouse")
    public String create(@RequestBody WarehouseDto warehouseDto) {
        log.info("warehouseDto qwe = " + warehouseDto);
        personRepository.save(new Person("Dostoevsky"));

        System.out.println("\n\n\n----------------------------------------------\n\n");
        System.out.println("Авторы в БД:");
        personRepository.findAll().forEach(p -> System.out.println(p.getName()));
        System.out.println("\n\n----------------------------------------------\n\n\n");


        //if (mongoClient == null) {
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            //db = mongoClient.getDatabase("myMongoDb");
            db = mongoClient.getDatabase("company");
            collection = db.getCollection("places");
            collection.deleteMany(new Document());
            collection.createIndex(Indexes.geo2dsphere("location"));
            collection.insertOne(Document.parse("{'name':'Big Ben','location': {'coordinates':[-0.1268194,51.5007292],'type':'Point'}}"));
            collection.insertOne(Document.parse("{'name':'Hyde Park','location': {'coordinates': [[[-0.159381,51.513126],[-0.189615,51.509928],[-0.187373,51.502442], [-0.153019,51.503464],[-0.159381,51.513126]]],'type':'Polygon'}}"));
       // }

        Point currentLoc = new Point(new Position(-0.126821, 51.495885));
        FindIterable<Document> result = collection.find(Filters.near("location", currentLoc, 1000.0, 10.0));

        System.out.println("\n\n----------------------------------------------\n\n\n");
        System.out.println("is Big Ben = " + result.first().get("name"));


        return "WarehouseDto success";
    }

}

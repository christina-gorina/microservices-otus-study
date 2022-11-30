package com.christinagorina.logistics.controller;

import com.christinagorina.logistics.model.ProductItem;
import com.christinagorina.logistics.model.Reserve;
import com.christinagorina.logistics.model.Warehouse;
import com.christinagorina.logistics.model.WarehouseDto;
//TODO исправить
//import com.christinagorina.logistics.repository.WarehouseRepository;
//import com.mongodb.MongoClient;
import com.christinagorina.logistics.repostory.WarehouseRepository;
import com.mongodb.client.*;
import com.mongodb.client.model.Indexes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
@Slf4j
public class LogisticsController {
    //TODO исправить
    //private final WarehouseRepository warehouseRepository;
    private final MongoDatabase mongoDatabase;
    private final WarehouseRepository warehouseRepository;

    @PostMapping("/api/warehouse")
    public String create(@RequestBody WarehouseDto warehouseDto) {
        log.info("warehouseDto qwe = " + warehouseDto);

        List<ProductItem> productItemsNeedBuy = new ArrayList<>();
        ProductItem productItem1 = ProductItem.builder().uuid(UUID.fromString("16cb7a21-eb5e-4d41-a176-8f49ca425b0c")).warehouseCount(5).build();
        ProductItem productItem2 = ProductItem.builder().uuid(UUID.fromString("261e9635-3ed0-485f-8f28-56aa156958ea")).warehouseCount(30).build();
        ProductItem productItem3 = ProductItem.builder().uuid(UUID.fromString("3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8")).warehouseCount(10).build();
        productItemsNeedBuy.add(productItem1);
        productItemsNeedBuy.add(productItem2);
        productItemsNeedBuy.add(productItem3);

        MongoCollection<Document> collection = mongoDatabase.getCollection("warehouses");
        collection.createIndex(Indexes.geo2dsphere("location"));

        Point currentLocAnino = new Point(55.583037, 37.595277);
        //TODO еще один адрес с другого конца
        Point currentLocVarshavskaya = new Point(55.653307, 37.620642);
        Distance distance = new Distance(200, Metrics.KILOMETERS);

        GeoResults<Warehouse> gr = warehouseRepository.findByLocationNear(currentLocAnino, distance);
        List<Warehouse> warehouses = new ArrayList<>();
        //gr.forEach(r -> System.out.println(r.getContent().getName()));
        gr.forEach(r -> {
            warehouses.add(r.getContent());
        });
        System.out.println("==============================================");


        productItemsNeedBuy.stream().forEach(productItemNeedBuy -> {
            AtomicReference<Integer> reservedSave = new AtomicReference<>(0);
            IntStream.range(0, warehouses.size()).forEach(i -> {
                if (reservedSave.get() < productItemNeedBuy.getWarehouseCount()) {
                    warehouses.get(i).getProductItems().stream().filter(p -> productItemNeedBuy.getUuid().equals(p.getUuid())).findFirst()
                            .ifPresent(p -> {
                                Integer needBuy = productItemNeedBuy.getWarehouseCount() - reservedSave.get();
                                if (p.getWarehouseCount() >= needBuy) {
                                    reservedSave.set(productItemNeedBuy.getWarehouseCount());
                                    //TODO еще OrderUUID заполнить тут
                                    //TODO Reserve сделать полем в warehouse productItems
                                    warehouses.get(i).getReserve().add(createReserve(p, needBuy));
                                } else {
                                    reservedSave.set(reservedSave.get() + p.getWarehouseCount());
                                    //TODO еще OrderUUID заполнить тут
                                    warehouses.get(i).getReserve().add(createReserve(p, p.getWarehouseCount()));
                                }
                            });
                }
            });
        });

        System.out.println("list warehouses qwe = " + warehouses);

        return "WarehouseDto succegss";
    }

    private Reserve createReserve(ProductItem productItem, Integer count) {
        return Reserve.builder().productItemUUID(productItem.getUuid()).count(count).build();
    }

}

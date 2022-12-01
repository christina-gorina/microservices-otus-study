package com.christinagorina.logistics.service;

import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.events.logistics.LogisticsEvent;
import com.christinagorina.logistics.model.OrdersReserve;
import com.christinagorina.logistics.model.ProductItem;
import com.christinagorina.logistics.model.Reserve;
import com.christinagorina.logistics.model.Warehouse;
import com.christinagorina.logistics.repostory.OrdersReserveRepository;
import com.christinagorina.logistics.repostory.WarehouseRepository;
import com.christinagorina.status.OrderStatus;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseService {

    private final MongoDatabase mongoDatabase;
    private final WarehouseRepository warehouseRepository;
    private final OrdersReserveRepository ordersReserveRepository;
    private final Sinks.Many<Message<LogisticsEvent>> logisticsSink;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void warehouseReserve(Message<CatalogEvent> catalogEventMsg) {
        CatalogEvent catalogEvent = catalogEventMsg.getPayload();
        if(!OrderStatus.RESERVED.equals(catalogEvent.getStatus())){
            return;
        }

        Optional<OrdersReserve> ordersReserve = ordersReserveRepository.findByUuid(catalogEvent.getOrderUuid());
        log.info("qwe ordersReserve = " + ordersReserve);
        if (ordersReserve.isPresent()) {
            log.info("qwe2");
            return;
        }
        log.info("qwe3");
        OrdersReserve ordersReserveNew = ordersReserveRepository.save(OrdersReserve.builder().uuid(catalogEvent.getOrderUuid()).build());
        log.info("qwe4 ordersReserveNew = " + ordersReserveNew);

        MongoCollection<Document> collection = mongoDatabase.getCollection("warehouses");
        collection.createIndex(Indexes.geo2dsphere("location"));

        Point currentLocAnino = new Point(catalogEvent.getAddressX(), catalogEvent.getAddressY());
        //TODO еще один адрес с другого конца
        Point currentLocVarshavskaya = new Point(55.653307, 37.620642);
        Distance distance = new Distance(200, Metrics.KILOMETERS);

        GeoResults<Warehouse> gr = warehouseRepository.findByLocationNear(currentLocAnino, distance);
        List<Warehouse> warehouses = new ArrayList<>();
        gr.forEach(r -> warehouses.add(r.getContent()));
        System.out.println("==============================================");

        catalogEvent.getProductItemsUuidAndCount().forEach((itemUuid, count) -> {
            AtomicReference<Integer> reservedSave = new AtomicReference<>(0);
            IntStream.range(0, warehouses.size()).forEach(i -> {
                if (reservedSave.get() < count) {
                    warehouses.get(i).getProductItems().stream().filter(p -> itemUuid.equals(p.getUuid()) && p.getWarehouseCount() > 0).findFirst()
                            .ifPresent(p -> {
                                Integer needBuy = count - reservedSave.get();
                                if (p.getWarehouseCount() >= needBuy) {
                                    reservedSave.set(count);
                                    //TODO в подтверждающей транзакции после списания денег менять статус на готов к сборке
                                    p.setWarehouseCount(p.getWarehouseCount() - needBuy);
                                    warehouses.get(i).getReserve().add(createReserve(p, needBuy, catalogEvent.getOrderUuid()));
                                } else {
                                    reservedSave.set(reservedSave.get() + p.getWarehouseCount());
                                    p.setWarehouseCount(0);
                                    warehouses.get(i).getReserve().add(createReserve(p, p.getWarehouseCount(), catalogEvent.getOrderUuid()));
                                }
                            });
                }
            });
        });

        List<Warehouse> warehouseList = warehouseRepository.saveAll(warehouses);
        log.info("qwe warehouseList = " + warehouseList);
        //TODO когда буду статус менять, то искать по Reserve, мб там id надо задать

        Message<LogisticsEvent> logisticsEventMsg = MessageBuilder
                .withPayload(LogisticsEvent.builder()
                        .orderUuid(catalogEvent.getOrderUuid())
                        .orderStatus(OrderStatus.RESERVED)
                        .build())
                .setHeader(KafkaHeaders.MESSAGE_KEY, catalogEvent.getOrderUuid())
                .build();

        log.info("logisticsEventMsg qwe = " + logisticsEventMsg);
        Sinks.EmitResult emitResult = logisticsSink.tryEmitNext(logisticsEventMsg);

        if (emitResult.isSuccess()) {
            Acknowledgment acknowledgment = catalogEventMsg.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
            if (Objects.nonNull(acknowledgment)) {
                log.info("acknowledgment != null qwe");
                acknowledgment.acknowledge();
                log.info("acknowledgment.acknowledge qwe");
            }
        } else {
            log.info("emitResult.orThrow");
            emitResult.orThrow();
        }
    }

    private Reserve createReserve(ProductItem productItem, Integer count, UUID orderuuid) {
        return Reserve.builder()
                .id(UUID.randomUUID())
                .productItemUUID(productItem.getUuid())
                .count(count)
                .orderStatus(OrderStatus.RESERVED)
                .orderUUID(orderuuid)
                .build();
    }
}

package com.christinagorina.logistics.service;

import com.christinagorina.events.BillingEvent;
import com.christinagorina.events.CatalogEvent;
import com.christinagorina.events.LogisticsEvent;
import com.christinagorina.events.ReserveOnWarehouse;
import com.christinagorina.logistics.model.OrdersIdempotent;
import com.christinagorina.logistics.model.ProductItem;
import com.christinagorina.logistics.model.Reserve;
import com.christinagorina.logistics.model.Warehouse;
import com.christinagorina.logistics.repostory.OrdersoIdempotentRepository;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseService {

    private final MongoDatabase mongoDatabase;
    private final WarehouseRepository warehouseRepository;
    private final OrdersoIdempotentRepository ordersoIdempotentRepository;
    private final Sinks.Many<Message<LogisticsEvent>> logisticsSink;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class) //SERIALIZABLE т.к. товары могут добавляться и возможно фантомное чтение
    public void warehouseReserve(Message<CatalogEvent> catalogEventMsg) {
        CatalogEvent catalogEvent = catalogEventMsg.getPayload();
        Optional<OrdersIdempotent> ordersIdempotent = ordersoIdempotentRepository.findByOrderId(catalogEvent.getOrderId());
        log.info("warehouseReserve ordersReserve = " + ordersIdempotent);
        if (ordersIdempotent.isPresent()) {
            log.info("Already exist");
            acknowledgeEvent(catalogEventMsg);
            return;
        }

        Message<LogisticsEvent> logisticsEventMsg;

        if(OrderStatus.RESERVE_REJECTED.equals(catalogEvent.getStatus())){
            logisticsEventMsg = createLogisticsEvent(catalogEvent, catalogEvent.getStatus(), "Order rejected", null);
        } else {
            AtomicReference<String> userMessage = new AtomicReference<>();
            userMessage.set("");
            List<ReserveOnWarehouse> reserveOnWarehouses = new ArrayList<>();
            log.info("warehouseReserve next");
            OrdersIdempotent ordersIdempotentNew = ordersoIdempotentRepository.save(OrdersIdempotent.builder().orderId(catalogEvent.getOrderId()).build());
            log.info("warehouseReserve ordersIdempotentNew = " + ordersIdempotentNew);

            MongoCollection<Document> collection = mongoDatabase.getCollection("warehouses");
            collection.createIndex(Indexes.geo2dsphere("location"));
            Point currentLocAnino = new Point(catalogEvent.getAddressX(), catalogEvent.getAddressY());
            Distance distance = new Distance(200, Metrics.KILOMETERS);
            GeoResults<Warehouse> gr = warehouseRepository.findByLocationNear(currentLocAnino, distance);
            List<Warehouse> warehouses = new ArrayList<>();
            gr.forEach(r -> warehouses.add(r.getContent()));



            catalogEvent.getProductItemsUuidAndCount().forEach((itemUuid, count) -> {
                AtomicReference<Integer> reservedSave = new AtomicReference<>(0);
                IntStream.range(0, warehouses.size()).forEach(i -> {
                    if (reservedSave.get() < count) {
                        warehouses.get(i).getProductItems().stream().filter(p -> itemUuid.equals(p.getUuid()) && p.getWarehouseCount() > 0).findFirst()
                                .ifPresent(p -> {
                                    Integer needBuy = count - reservedSave.get();
                                    if (p.getWarehouseCount() >= needBuy) {
                                        reservedSave.set(count);
                                        ReserveOnWarehouse reserveOnWarehouse = createReserveOnWarehouse(p, needBuy, warehouses.get(i).getName());
                                        reserveOnWarehouses.add(reserveOnWarehouse);
                                        Reserve reserve = createReserve(p, needBuy, catalogEvent.getOrderId(), warehouses.get(i).getName());
                                        warehouses.get(i).getReserveList().add(reserve);
                                        userMessage.set(userMessage + "Товар " + p.getName() +
                                                " будет доставлен со склада " + warehouses.get(i).getName() +
                                                " в количестве " + needBuy +
                                                ";\n");
                                        p.setWarehouseCount(p.getWarehouseCount() - needBuy);
                                    } else {
                                        reservedSave.set(reservedSave.get() + p.getWarehouseCount());
                                        ReserveOnWarehouse reserveOnWarehouse = createReserveOnWarehouse(p, p.getWarehouseCount(), warehouses.get(i).getName());
                                        reserveOnWarehouses.add(reserveOnWarehouse);
                                        warehouses.get(i).getReserveList().add(createReserve(p, p.getWarehouseCount(), catalogEvent.getOrderId(), warehouses.get(i).getName()));
                                        userMessage.set(userMessage + "Товар " + p.getName() +
                                                " будет доставлен со склада " + warehouses.get(i).getName() +
                                                " в количестве " + p.getWarehouseCount() +
                                                ";\n");
                                        p.setWarehouseCount(0);
                                    }
                                });
                    }
                });
            });

            List<Warehouse> warehouseList = warehouseRepository.saveAll(warehouses);
            log.info("warehouseReserve warehouseList = " + warehouseList);

            logisticsEventMsg = createLogisticsEvent(catalogEvent, catalogEvent.getStatus(), userMessage.get(), reserveOnWarehouses);
        }

        log.info("warehouseReserve logisticsEventMsg = " + logisticsEventMsg);
        Sinks.EmitResult emitResult = logisticsSink.tryEmitNext(logisticsEventMsg);

        if (emitResult.isSuccess()) {
            acknowledgeEvent(catalogEventMsg);
        } else {
            log.info("emitResult.orThrow");
            emitResult.orThrow();
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void saveResult(BillingEvent billingEvent) {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        log.info("saveResult warehouses = " + warehouses);
        OrderStatus orderStatus = Optional.of(billingEvent).map(BillingEvent::getOrderStatus).orElseThrow();
        Long orderId = Optional.of(billingEvent).map(BillingEvent::getOrderId).orElseThrow();

        billingEvent.getReserveOnWarehouses().forEach(r -> {
            String warehouseName = r.getWarehouseName();
            Integer count = r.getCount();
            UUID productItemUUID = r.getProductItemUUID();
            Warehouse warehouse = warehouses.stream().filter(w -> warehouseName.equals(w.getName())).findFirst().orElseThrow();
            if(OrderStatus.PAYMENT_REJECTED.equals(orderStatus)){
                ProductItem productItem = warehouse.getProductItems().stream().filter(p -> p.getUuid().equals(productItemUUID)).findFirst().orElseThrow();
                productItem.setWarehouseCount(productItem.getWarehouseCount() + count);
            }
            warehouse.getReserveList().stream().filter(reserve -> orderId.equals(reserve.getOrderId())).forEach(reserve -> {
                reserve.setOrderStatus(orderStatus);
            });
            warehouseRepository.save(warehouse);
        });
    }

    public Message<LogisticsEvent> createLogisticsEvent(CatalogEvent catalogEvent, OrderStatus orderStatus, String userMessage, List<ReserveOnWarehouse> reserveOnWarehouses) {
        return MessageBuilder
                .withPayload(LogisticsEvent.builder()
                        .orderId(catalogEvent.getOrderId())
                        .orderStatus(orderStatus)
                        .userName(catalogEvent.getUserName())
                        .userMessage(userMessage)
                        .productItemsUuidAndCount(catalogEvent.getProductItemsUuidAndCount())
                        .reserveOnWarehouses(reserveOnWarehouses)
                        .build())
                .setHeader(KafkaHeaders.MESSAGE_KEY, catalogEvent.getOrderId())
                .build();
    }

    private void acknowledgeEvent(Message<CatalogEvent> catalogEventMsg) {
        Acknowledgment acknowledgment = catalogEventMsg.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if (Objects.nonNull(acknowledgment)) {
            log.info("acknowledgment != null");
            acknowledgment.acknowledge();
            log.info("acknowledgment.acknowledge");
        }
    }

    private ReserveOnWarehouse createReserveOnWarehouse(ProductItem productItem, Integer count, String warehouseName) {
        return ReserveOnWarehouse.builder()
                .productItemUUID(productItem.getUuid())
                .count(count)
                .warehouseName(warehouseName)
                .build();
    }

    private Reserve createReserve(ProductItem productItem, Integer count, Long orderId, String warehouseName) {
        return Reserve.builder()
                .id(UUID.randomUUID())
                .productItemUUID(productItem.getUuid())
                .count(count)
                .orderStatus(OrderStatus.RESERVED)
                .orderId(orderId)
                .warehouseName(warehouseName)
                .build();
    }
}

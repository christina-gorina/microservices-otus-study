package com.christinagorina.catalog.service;

import com.christinagorina.catalog.model.OrdersReserve;
import com.christinagorina.catalog.repository.OrdersReserveRepository;
import com.christinagorina.catalog.repository.ProductItemRepository;
import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.status.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.Message;
import reactor.core.publisher.Sinks;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final ProductItemRepository productItemRepository;
    private final OrdersReserveRepository ordersReserveRepository;
    private final Sinks.Many<Message<CatalogEvent>> catalogSink;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void productItemReservation(Message<OrderEvent> orderEventMsg) {
        OrderEvent orderEvent = orderEventMsg.getPayload();
        if(!OrderStatus.NEW.equals(orderEvent.getOrderStatus())){
            return;
            //TODO Везде, во всех сервисах в такой ситуации (где return) отсылать ask иначе сообщение так и будет приходить
        }
        //Todo можно этот момент рассказать на защите
        log.info("qwe1");
        log.info("qwe1_1 orderEvent.getOrderUuid() = " + orderEvent.getOrderUuid());
        Optional<OrdersReserve> ordersReserve = ordersReserveRepository.findByUuid(orderEvent.getOrderUuid());
        log.info("qwe ordersReserve = " + ordersReserve);
        if (ordersReserve.isPresent()) {
            log.info("qwe2");
            //TODO Везде, во всех сервисах в такой ситуации (где return) отсылать ask иначе сообщение так и будет приходить
            return;
        }
        log.info("qwe3");
        OrdersReserve ordersReserveNew = ordersReserveRepository.save(OrdersReserve.builder().uuid(orderEvent.getOrderUuid()).orderStatus(OrderStatus.RESERVED).build());
        log.info("qwe4 ordersReserveNew = " + ordersReserveNew);

        boolean checkCountCorrect = orderEvent.getProductItemsUuidAndCount().entrySet().stream()
                .allMatch(e -> checkCount(e.getKey(), e.getValue()));

        log.info("qwe5 checkCountCorrect = " + checkCountCorrect);
        CatalogEvent catalogEvent = CatalogEvent.builder()
                .addressX(orderEvent.getAddressX())
                .addressY(orderEvent.getAddressY())
                .orderUuid(orderEvent.getOrderUuid())
                .productItemsUuidAndCount(orderEvent.getProductItemsUuidAndCount())
                .userId(orderEvent.getUserId())
                .build();

        if (!checkCountCorrect) {
            log.info("productItemReservation state REJECTED");
            catalogEvent.setStatus(OrderStatus.REJECTED);
            log.info("qwe6 catalogEvent = " + catalogEvent);
            //TODO здесь сделать сагу откат, то есть кидать в другой топик
            //TODO Везде, во всех сервисах в такой ситуации (где return) отсылать ask иначе сообщение так и будет приходить
            return;
        } else {
            orderEvent.getProductItemsUuidAndCount().forEach(this::reserve);
            log.info("productItemReservation state RESERVED");
            catalogEvent.setStatus(OrderStatus.RESERVED);
            log.info("qwe6 catalogEvent = " + catalogEvent);
        }

        Message<CatalogEvent> catalogEventMsg = MessageBuilder
                .withPayload(catalogEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, catalogEvent.getOrderUuid())
                .build();

        log.info("catalogEventMsg qwe = " + catalogEventMsg);

        Sinks.EmitResult emitResult = catalogSink.tryEmitNext(catalogEventMsg);

        if (emitResult.isSuccess()) {
            log.info("emitResult.isSuccess qwe");
            Acknowledgment acknowledgment = orderEventMsg.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
            if (Objects.nonNull(acknowledgment)) {
                log.info("acknowledgment != null qwe");
                acknowledgment.acknowledge();
                log.info("acknowledgment.acknowledge qwe");
            }
        } else {
            log.info("emitResult.orThrow");
            emitResult.orThrow();
        }
        log.info("catalogEvent after");
    }

    private Boolean checkCount(UUID uuid, Integer count) {
        return productItemRepository.findByUuid(uuid)
                .filter(productItem -> productItem.getCount() - count > 0)
                .isPresent();
    }

    private void reserve(UUID uuid, Integer count) {
        productItemRepository.findByUuid(uuid)
                .map(productItem -> {
                    productItem.setCount(productItem.getCount() - count);
                    return productItem;
                }).orElseThrow();
    }

}

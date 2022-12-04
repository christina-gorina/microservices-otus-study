package com.christinagorina.catalog.service;

import com.christinagorina.catalog.model.OrdersIdempotent;
import com.christinagorina.catalog.repository.OrdersIdempotentRepository;
import com.christinagorina.catalog.repository.ProductItemRepository;
import com.christinagorina.events.CatalogEvent;
import com.christinagorina.events.OrderEvent;
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
    private final OrdersIdempotentRepository ordersIdempotentRepository;
    private final Sinks.Many<Message<CatalogEvent>> catalogSink;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void productItemReservation(Message<OrderEvent> orderEventMsg) {
        OrderEvent orderEvent = orderEventMsg.getPayload();
        log.info("qwe1_1 orderEvent.getOrderId() = " + orderEvent.getOrderId());
        Optional<OrdersIdempotent> ordersIdempotent = ordersIdempotentRepository.findByOrderId(orderEvent.getOrderId());
        log.info("qwe ordersIdempotent = " + ordersIdempotent);
        if (ordersIdempotent.isPresent()) {
            log.info("qwe2");
            acknowledgeEvent(orderEventMsg);
            return;
        }
        log.info("qwe3");
        OrdersIdempotent ordersIdempotentNew = ordersIdempotentRepository.save(OrdersIdempotent.builder().orderId(orderEvent.getOrderId()).orderStatus(orderEvent.getOrderStatus()).build());
        log.info("qwe4 ordersIdempotentNew = " + ordersIdempotentNew);

        boolean checkCountCorrect = orderEvent.getProductItemsUuidAndCount().entrySet().stream()
                .allMatch(e -> checkCount(e.getKey(), e.getValue()));

        log.info("qwe5 checkCountCorrect = " + checkCountCorrect);
        CatalogEvent catalogEvent = CatalogEvent.builder()
                .addressX(orderEvent.getAddressX())
                .addressY(orderEvent.getAddressY())
                .orderId(orderEvent.getOrderId())
                .productItemsUuidAndCount(orderEvent.getProductItemsUuidAndCount())
                .userId(orderEvent.getUserId())
                .build();

        if (!checkCountCorrect) {
            log.info("productItemReservation state REJECTED");
            ordersIdempotentNew.setOrderStatus(OrderStatus.REJECTED);
            catalogEvent.setStatus(OrderStatus.REJECTED);
            log.info("qwe6 catalogEvent = " + catalogEvent);
            //TODO здесь сделать сагу откат, то есть кидать в другой топик
            acknowledgeEvent(orderEventMsg);
            return;
        } else {
            orderEvent.getProductItemsUuidAndCount().forEach(this::reserve);
            log.info("productItemReservation state RESERVED");
            ordersIdempotentNew.setOrderStatus(OrderStatus.RESERVED);
            catalogEvent.setStatus(OrderStatus.RESERVED);
            log.info("qwe6 catalogEvent = " + catalogEvent);
        }
        ordersIdempotentRepository.save(ordersIdempotentNew);

        Message<CatalogEvent> catalogEventMsg = MessageBuilder
                .withPayload(catalogEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, catalogEvent.getOrderId())
                .build();

        log.info("catalogEventMsg qwe = " + catalogEventMsg);

        Sinks.EmitResult emitResult = catalogSink.tryEmitNext(catalogEventMsg);

        if (emitResult.isSuccess()) {
            log.info("emitResult.isSuccess qwe");
            acknowledgeEvent(orderEventMsg);
        } else {
            log.info("emitResult.orThrow");
            emitResult.orThrow();
        }
        log.info("catalogEvent after");
    }

    private void acknowledgeEvent(Message<OrderEvent> orderEventMsg) {
        Acknowledgment acknowledgment = orderEventMsg.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if (Objects.nonNull(acknowledgment)) {
            log.info("acknowledgment != null qwe");
            acknowledgment.acknowledge();
            log.info("acknowledgment.acknowledge qwe");
        }
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

package com.christinagorina.catalog.service;

import com.christinagorina.catalog.model.OrdersIdempotent;
import com.christinagorina.catalog.model.ProductItem;
import com.christinagorina.catalog.repository.OrdersIdempotentRepository;
import com.christinagorina.catalog.repository.ProductItemRepository;
import com.christinagorina.events.BillingEvent;
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

import java.util.Map;
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
        log.info("orderEvent.getOrderId() = " + orderEvent.getOrderId());
        Optional<OrdersIdempotent> ordersIdempotent = ordersIdempotentRepository.findByOrderId(orderEvent.getOrderId());
        log.info("ordersIdempotent = " + ordersIdempotent);
        if (ordersIdempotent.isPresent()) {
            log.info("Already exist");
            acknowledgeEvent(orderEventMsg);
            return;
        }
        OrdersIdempotent ordersIdempotentNew = ordersIdempotentRepository.save(OrdersIdempotent.builder().orderId(orderEvent.getOrderId()).orderStatus(orderEvent.getOrderStatus()).build());
        log.info("ordersIdempotentNew = " + ordersIdempotentNew);

        boolean checkCountCorrect = orderEvent.getProductItemsUuidAndCount().entrySet().stream()
                .allMatch(e -> checkCount(e.getKey(), e.getValue()));

        log.info("checkCountCorrect = " + checkCountCorrect);
        CatalogEvent catalogEvent = CatalogEvent.builder()
                .addressX(orderEvent.getAddressX())
                .addressY(orderEvent.getAddressY())
                .orderId(orderEvent.getOrderId())
                .productItemsUuidAndCount(orderEvent.getProductItemsUuidAndCount())
                .userId(orderEvent.getUserId())
                .build();

        if (!checkCountCorrect) {
            log.info("productItemReservation state RESERVE_REJECTED");
            ordersIdempotentNew.setOrderStatus(OrderStatus.RESERVE_REJECTED);
            catalogEvent.setStatus(OrderStatus.RESERVE_REJECTED);
            log.info("catalogEvent = " + catalogEvent);
        } else {
            orderEvent.getProductItemsUuidAndCount().forEach(this::reserve);
            log.info("productItemReservation state RESERVED");
            ordersIdempotentNew.setOrderStatus(OrderStatus.RESERVED);
            catalogEvent.setStatus(OrderStatus.RESERVED);
            log.info("catalogEvent = " + catalogEvent);
        }
        ordersIdempotentRepository.save(ordersIdempotentNew);

        Message<CatalogEvent> catalogEventMsg = MessageBuilder
                .withPayload(catalogEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, catalogEvent.getOrderId())
                .build();

        log.info("catalogEventMsg = " + catalogEventMsg);

        Sinks.EmitResult emitResult = catalogSink.tryEmitNext(catalogEventMsg);

        if (emitResult.isSuccess()) {
            log.info("emitResult.isSuccess");
            acknowledgeEvent(orderEventMsg);
        } else {
            log.info("emitResult.orThrow");
            emitResult.orThrow();
        }
        log.info("catalogEvent after");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void saveResult(BillingEvent billingEvent) {
        OrdersIdempotent ordersIdempotent = ordersIdempotentRepository.findByOrderId(billingEvent.getOrderId()).orElseThrow();
        ordersIdempotent.setOrderStatus(billingEvent.getOrderStatus());
        ordersIdempotent = ordersIdempotentRepository.save(ordersIdempotent);
        if(OrderStatus.PAYMENT_REJECTED.equals(billingEvent.getOrderStatus())){
            Map<UUID, Integer> productItemsUuidAndCount = billingEvent.getProductItemsUuidAndCount();
            productItemsUuidAndCount.forEach((key, value) -> {
                ProductItem productItem = productItemRepository.findByUuid(key).orElseThrow();
                productItem.setCount(productItem.getCount() + value);
                productItemRepository.save(productItem);
            });
        }
        log.info("saveResult ordersIdempotent = " + ordersIdempotent);
    }

    private void acknowledgeEvent(Message<OrderEvent> orderEventMsg) {
        Acknowledgment acknowledgment = orderEventMsg.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if (Objects.nonNull(acknowledgment)) {
            log.info("acknowledgment != null");
            acknowledgment.acknowledge();
            log.info("acknowledgment.acknowledge");
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

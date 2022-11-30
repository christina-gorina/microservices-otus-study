package com.christinagorina.catalog.service;

import com.christinagorina.catalog.model.OrdersReserve;
import com.christinagorina.catalog.repository.OrdersReserveRepository;
import com.christinagorina.catalog.repository.ProductItemRepository;
import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.status.CatalogStatus;
import com.christinagorina.events.order.OrderEvent;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final ProductItemRepository productItemRepository;
    private final OrdersReserveRepository ordersReserveRepository;
    private final Sinks.Many<Message<CatalogEvent>> catalogSink;

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void productItemReservation(Message<OrderEvent> orderEventMsg) {
        OrderEvent orderEvent = orderEventMsg.getPayload();
        //Todo можно этот момент рассказать на защите
        log.info("qwe1");
        log.info("qwe1_1 orderEvent.getUuid() = " + orderEvent.getUuid());
        Optional<OrdersReserve> ordersReserve = ordersReserveRepository.findByUuid(orderEvent.getUuid());
        log.info("qwe ordersReserve = " + ordersReserve);
        if (ordersReserve.isPresent()) {
            log.info("qwe2");
            return;
        }
        log.info("qwe3");
        OrdersReserve ordersReserveNew = ordersReserveRepository.save(OrdersReserve.builder().uuid(orderEvent.getUuid()).build());
        log.info("qwe4 ordersReserveNew = " + ordersReserveNew);

        boolean checkCountCorrect = orderEvent.getProductItemsIdAndCount().entrySet().stream()
                .allMatch(e -> checkCount(e.getKey(), e.getValue()));
        log.info("qwe5 checkCountCorrect = " + checkCountCorrect);
        CatalogEvent catalogEvent = CatalogEvent.builder()
                .addressX(orderEvent.getAddressX())
                .addressY(orderEvent.getAddressY())
                .orderuuid(orderEvent.getUuid())
                .productItemsIdAndCount(orderEvent.getProductItemsIdAndCount())
                .build();

        if (!checkCountCorrect) {
            log.info("productItemReservation state REJECTED");
            catalogEvent.setStatus(CatalogStatus.REJECTED);
            log.info("qwe6 catalogEvent = " + catalogEvent);
            //TODO здесь сделать сагу откат, то есть кидать в другой топик
            return;
        } else{
            log.info("productItemReservation state RESERVED");
            catalogEvent.setStatus(CatalogStatus.RESERVED);
            log.info("qwe6 catalogEvent = " + catalogEvent);
        }

        Message<CatalogEvent> catalogEventMsg = MessageBuilder
                .withPayload(catalogEvent)
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

    private Boolean checkCount(Long id, Integer count) {
        return productItemRepository.findById(id)
                .filter(productItem -> productItem.getCount() - count > 0)
                .isPresent();
    }

}

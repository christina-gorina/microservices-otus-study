package com.christinagorina.billing.service;

import com.christinagorina.billing.repository.AccountRepository;
import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.events.logistics.LogisticsEvent;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.payment.BillingEvent;
import com.christinagorina.status.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final AccountRepository accountRepository;
    private final Sinks.Many<Message<BillingEvent>> resultSink;

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public BillingEvent check(OrderEvent orderEvent, LogisticsEvent logisticsEvent) {
        log.info("orderEvent checkOrder qwe {}", orderEvent);
        log.info("logisticsEvent checkOrder qwe {}", logisticsEvent);
        if(!(OrderStatus.NEW.equals(orderEvent.getOrderStatus()) || OrderStatus.RESERVED.equals(logisticsEvent.getOrderStatus()))){
            log.info("billingEvent final REJECTED");
            return BillingEvent.builder().orderId(orderEvent.getOrderId()).orderStatus(OrderStatus.REJECTED).build();
        }
        BillingEvent billingEvent = paymentReservation(orderEvent);
        log.info("billingEvent final qwe {}", billingEvent);

        Message<BillingEvent> billingEventMsg = MessageBuilder
                .withPayload(billingEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, billingEvent.getOrderId())
                .build();

        Sinks.EmitResult emitResult = resultSink.tryEmitNext(billingEventMsg);

        if (emitResult.isSuccess()) {
            log.info("emitResult.isSuccess qwe");
        } else {
            log.info("emitResult.error qwe ");
        }


        return billingEvent;
    }

    private BillingEvent paymentReservation(OrderEvent orderEvent) {
        return accountRepository.findById(orderEvent.getUserId())
                .filter(account -> account.getBalance().compareTo(orderEvent.getPrice()) >= 0)
                .map(account -> {
                    account.setBalance(account.getBalance().subtract(orderEvent.getPrice()));
                    return BillingEvent.builder()
                            .orderStatus(OrderStatus.COMPLETED)
                            .orderId(orderEvent.getOrderId())
                            .build();
                })
                .orElse(BillingEvent.builder()
                        .orderStatus(OrderStatus.REJECTED)
                        .orderId(orderEvent.getOrderId())
                        .build());
    }

}

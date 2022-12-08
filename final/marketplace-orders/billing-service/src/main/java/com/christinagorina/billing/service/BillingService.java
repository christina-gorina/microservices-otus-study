package com.christinagorina.billing.service;

import com.christinagorina.billing.model.OrdersIdempotent;
import com.christinagorina.billing.repository.AccountRepository;
import com.christinagorina.billing.repository.OrdersIdempotentRepository;
import com.christinagorina.events.LogisticsEvent;
import com.christinagorina.events.OrderEvent;
import com.christinagorina.events.BillingEvent;
import com.christinagorina.status.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final AccountRepository accountRepository;
    private final Sinks.Many<Message<BillingEvent>> resultSink;
    private final OrdersIdempotentRepository ordersIdempotentRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void check(OrderEvent orderEvent, LogisticsEvent logisticsEvent) {
        log.info("orderEvent check = " + orderEvent);
        log.info("logisticsEvent check = " + logisticsEvent);
        Optional<OrdersIdempotent> ordersIdempotent = ordersIdempotentRepository.findByOrderId(logisticsEvent.getOrderId());
        log.info("ordersIdempotent = " + ordersIdempotent);
        if (ordersIdempotent.isPresent()) {
            //TODO если прогнать любой сценарий, отключить сервисы, а потом запустить этот сервис, то он все равно получит из кафки последнее сообщение,
            // сделать отправку подтверждения здесь, как в остальных сервисах

            //TODO написать 3 сценария для тестов, набить тестовые данные

            //TODO возможно подключить регистрацию
            log.info("Already exist");
            return;
        }
        ordersIdempotentRepository.save(OrdersIdempotent.builder().orderId(logisticsEvent.getOrderId()).orderStatus(logisticsEvent.getOrderStatus()).build());
        log.info("orderEvent checkOrder {}", orderEvent);
        log.info("logisticsEvent checkOrder {}", logisticsEvent);
        Optional.ofNullable(orderEvent).filter(o -> OrderStatus.NEW.equals(orderEvent.getOrderStatus())).orElseThrow();
        OrderStatus orderStatus;
        if(!OrderStatus.RESERVED.equals(logisticsEvent.getOrderStatus())){
            log.info("billingEvent final REJECTED");
            orderStatus = logisticsEvent.getOrderStatus();
        } else {
            orderStatus = paymentReservation(orderEvent);
        }
        String userMessage;
        if(OrderStatus.COMPLETED.equals(orderStatus)){
            userMessage = logisticsEvent.getUserMessage();
        } else {
            userMessage = "Order rejected";
        }

        BillingEvent billingEvent = createBillingEvent(orderEvent, logisticsEvent, orderStatus, userMessage);
        log.info("billingEvent final {}", billingEvent);

        Message<BillingEvent> billingEventMsg = MessageBuilder
                .withPayload(billingEvent)
                .setHeader(KafkaHeaders.MESSAGE_KEY, billingEvent.getOrderId())
                .build();

        Sinks.EmitResult emitResult = resultSink.tryEmitNext(billingEventMsg);

        if (emitResult.isSuccess()) {
            log.info("emitResult.isSuccess");
        } else {
            log.info("emitResult.error");
        }
    }

    private OrderStatus paymentReservation(OrderEvent orderEvent) {
        return accountRepository.findById(orderEvent.getUserId())
                .filter(account -> account.getBalance().compareTo(orderEvent.getPrice()) >= 0)
                .map(account -> {
                    account.setBalance(account.getBalance().subtract(orderEvent.getPrice()));
                    return OrderStatus.COMPLETED;
                })
                .orElse(OrderStatus.PAYMENT_REJECTED);
    }

    private BillingEvent createBillingEvent(OrderEvent orderEvent, LogisticsEvent logisticsEvent, OrderStatus orderStatus, String userMessage) {
        return BillingEvent.builder()
                .orderStatus(orderStatus)
                .orderId(orderEvent.getOrderId())
                .userMessage(userMessage)
                .reserveOnWarehouses(logisticsEvent.getReserveOnWarehouses())
                .productItemsUuidAndCount(logisticsEvent.getProductItemsUuidAndCount())
                .build();
    }

}

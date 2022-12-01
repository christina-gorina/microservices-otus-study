package com.christinagorina.billing.config;

import com.christinagorina.billing.service.BillingService;
import com.christinagorina.events.logistics.LogisticsEvent;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.payment.BillingEvent;
import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Configuration
@Slf4j
@AllArgsConstructor
public class BillingConfig {

   /* private final BillingService billingService;

    @Bean
    public Function<Flux<OrderEvent>, Flux<BillingEvent>> paymentProcessor() {
        return flux -> flux.flatMap(this::processPayment);
    }

    private Mono<BillingEvent> processPayment(OrderEvent orderEvent) {
        if(OrderStatus.NEW.equals(orderEvent.getOrderStatus())){
            return Mono.fromSupplier(() -> billingService.paymentReservation(orderEvent));
        } else {
            return Mono.fromRunnable(() -> billingService.cancelPaymentReservation(orderEvent));
        }
    }*/

    @Bean
    public BiConsumer<KStream<String, OrderEvent>, KStream<String, LogisticsEvent>> billingConsumer() {
        return (orderEvent, logisticsEvent) -> {
            orderEvent.peek((k, v) -> {
                log.info("orderEvent({}): {}", k, v);
            });
            logisticsEvent.peek((k, v) -> {
                log.info("logisticsEvent({}): {}", k, v);
            });
        };

    }

}


package com.christinagorina.billing.config;

import com.christinagorina.billing.service.BillingService;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.payment.BillingEvent;
import com.christinagorina.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@Slf4j
@AllArgsConstructor
public class BillingConfig {

    private final BillingService billingService;

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
    }

}


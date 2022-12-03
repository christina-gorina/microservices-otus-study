package com.christinagorina.billing.config;

import com.christinagorina.events.logistics.LogisticsEvent;
import com.christinagorina.events.order.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import java.time.Duration;
import java.util.function.BiConsumer;
import org.apache.kafka.streams.kstream.KStream;

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
    //TODO поменяла ключ с uuid на long
    public BiConsumer<KStream<String, OrderEvent>, KStream<String, LogisticsEvent>> billingConsumer() {
        //TODO сделать идемпотентность
        //TODO условие если ордер new здесь и во всех сервисах
        return (orderEventStream, logisticsEventStream) -> {
            orderEventStream.peek((m1, n1) -> log.info("orderEvent qwe = " + " m1 = " + m1 + " body1 = " + n1.getOrderUuid()));
            orderEventStream.peek((m2, n2) -> log.info("logisticsEvent qwe = " + " m2 = " + m2 + " body2 = " + n2.getOrderUuid()));
            orderEventStream
                    .selectKey((k1, v1) -> v1.getUserId())
                    .join(logisticsEventStream.selectKey((k2, v2) -> v2.getUserId()), //TODO getUserId просто для теста, так как он long
                            this::execute
                            , JoinWindows.of(Duration.ofSeconds(60))
                            , StreamJoined.with(Serdes.Long(), new JsonSerde<>(OrderEvent.class), new JsonSerde<>(LogisticsEvent.class))

                    )
                    .filter((s, event) -> {
                        if (null != event) {
                            log.info("Sending account event qwe {}", event);
                        }
                        return null != event;
                    });
        };

    }

/*    @Bean
    public BiConsumer<KStream<String, OrderEvent>, KTable<String, LogisticsEvent>> billingConsumer2() {
        //TODO сделать идемпотентность
        //TODO условие если ордер new здесь и во всех сервисах
        return (orderEventStream, logisticsEventStream) -> orderEventStream
                .leftJoin(logisticsEventStream,
                        (orderEvent, logisticsEvent) -> {
                            log.info("orderEvent =  {}", orderEvent);
                            log.info("logisticsEvent =  {}", logisticsEvent);
                            return null == logisticsEvent ? orderEvent : null;
                        })
                .filter((s, event) -> {
                    if (null != event) {
                        log.info("Sending account event {}", event);
                    }
                    return null != event;
                });

    }
    */

    private String execute(OrderEvent orderEvent, LogisticsEvent logisticsEvent) {
        log.info("orderEvent qwe {}", orderEvent);
        log.info("logisticsEvent qwe {}", logisticsEvent);
        return "sdfsdf";
    }
}


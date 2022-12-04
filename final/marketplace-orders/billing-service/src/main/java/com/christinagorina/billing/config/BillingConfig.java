package com.christinagorina.billing.config;

import com.christinagorina.billing.service.BillingService;
import com.christinagorina.events.logistics.LogisticsEvent;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.payment.BillingEvent;
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

    private final BillingService billingService;

    @Bean
    public BiConsumer<KStream<Long, OrderEvent>, KStream<Long, LogisticsEvent>> billingConsumer() {
        //TODO сделать идемпотентность
        //TODO условие если ордер new здесь и во всех сервисах
        //TODO здесь и во всех сервисах сделать изменение статуса в БД а не только уменьшение циферки (транзакционно)
        return (orderEventStream, logisticsEventStream) -> {
            orderEventStream.peek((m1, n1) -> log.info("orderEvent qwe" + " m1 = " + m1 + " orderEvent = " + n1));
            orderEventStream.peek((m2, n2) -> log.info("logisticsEvent qwe" + " m2 = " + m2 + " logisticsEvent = " + n2));
            orderEventStream
                    .selectKey((k1, v1) -> v1.getOrderId())
                    .join(logisticsEventStream.selectKey((k2, v2) -> v2.getOrderId()),
                            this::checkOrder
                            , JoinWindows.of(Duration.ofSeconds(60))
                            , StreamJoined.with(Serdes.Long(), new JsonSerde<>(OrderEvent.class), new JsonSerde<>(LogisticsEvent.class))
                    )
                    .filter((s, event) -> {
                        if (null != event) {
                            log.info("Sending account event qwe {}", event);
                        }
                        return null != event;
                    });;
        };

    }

    private BillingEvent checkOrder(OrderEvent orderEvent, LogisticsEvent logisticsEvent) {
        return billingService.check(orderEvent, logisticsEvent);
    }

    /*@Bean
    //TODO поменяла ключ с uuid на long
    public BiConsumer<KStream<String, OrderEvent>, KStream<String, LogisticsEvent>> billingProcessor() {
        //TODO сделать идемпотентность
        //TODO условие если ордер new здесь и во всех сервисах
        return (orderEventStream, logisticsEventStream) -> {
            orderEventStream.peek((m1, n1) -> log.info("orderEvent qwe = " + " m1 = " + m1 + " body1 = " + n1.getOrderId()));
            orderEventStream.peek((m2, n2) -> log.info("logisticsEvent qwe = " + " m2 = " + m2 + " body2 = " + n2.getOrderId()));
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

    private String execute(OrderEvent orderEvent, LogisticsEvent logisticsEvent) {
        log.info("orderEvent qwe {}", orderEvent);
        log.info("logisticsEvent qwe {}", logisticsEvent);
        return "sdfsdf";
    }*/
}


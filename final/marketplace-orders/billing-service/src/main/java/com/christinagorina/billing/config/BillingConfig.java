package com.christinagorina.billing.config;

import com.christinagorina.billing.service.BillingService;
import com.christinagorina.events.LogisticsEvent;
import com.christinagorina.events.OrderEvent;
import com.christinagorina.events.BillingEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import java.time.Duration;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.apache.kafka.streams.kstream.KStream;

@Configuration
@Slf4j
@AllArgsConstructor
public class BillingConfig {

    private final BillingService billingService;

    @Bean
    public BiConsumer<KStream<Long, OrderEvent>, KStream<Long, LogisticsEvent>> billingConsumer() {
        return (orderEventStream, logisticsEventStream) -> {
            orderEventStream.peek((m1, n1) -> log.info("orderEvent" + " m1 = " + m1 + " orderEvent = " + n1));
            logisticsEventStream.peek((m2, n2) -> log.info("logisticsEvent" + " m2 = " + m2 + " logisticsEvent = " + n2));
            orderEventStream
                    .selectKey((k1, v1) -> v1.getOrderId())
                    .join(logisticsEventStream.selectKey((k2, v2) -> v2.getOrderId()),
                            this::checkOrder
                            , JoinWindows.of(Duration.ofSeconds(60))
                            , StreamJoined.with(Serdes.Long(), new JsonSerde<>(OrderEvent.class), new JsonSerde<>(LogisticsEvent.class))
                    )
                    .peek((k, v) -> log.info("Events key = " + k + ", value = " +  v));
        };

    }

    private String checkOrder(OrderEvent orderEvent, LogisticsEvent logisticsEvent) {
        log.info("orderEvent = " + orderEvent);
        log.info("logisticsEvent = " + logisticsEvent);
        billingService.check(orderEvent, logisticsEvent);
        return "complete";
    }

}


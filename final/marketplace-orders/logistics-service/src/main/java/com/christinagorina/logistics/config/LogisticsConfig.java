package com.christinagorina.logistics.config;

import com.christinagorina.events.LogisticsEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
@Slf4j
@AllArgsConstructor
public class LogisticsConfig {

    @Bean
    public Sinks.Many<Message<LogisticsEvent>> logisticsSink() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<LogisticsEvent>>> logisticsSupplier(Sinks.Many<Message<LogisticsEvent>> sink) {
        return sink::asFlux;
    }

}


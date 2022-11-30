package com.christinagorina.catalog.config;

import com.christinagorina.events.catalog.CatalogEvent;
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
public class CatalogConfig {

    @Bean
    public Sinks.Many<Message<CatalogEvent>> catalogSink() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<CatalogEvent>>> catalogSupplier(Sinks.Many<Message<CatalogEvent>> sink) {
        return sink::asFlux;
    }

}


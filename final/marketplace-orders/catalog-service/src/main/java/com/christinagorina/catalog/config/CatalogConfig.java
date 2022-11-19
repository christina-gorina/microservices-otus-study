package com.christinagorina.catalog.config;

import com.christinagorina.catalog.service.InventoryService;
import com.christinagorina.events.order.OrderEvent;
import com.christinagorina.events.catalog.CatalogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@Slf4j
public class CatalogConfig {

    @Autowired
    private InventoryService service;

    @Bean
    public Function<Flux<OrderEvent>, Flux<CatalogEvent>> inventoryProcessor() {
        log.info("inventoryProcessor qwe");
        return flux -> flux.flatMap(this::processInventory);
    }

    private Mono<CatalogEvent> processInventory(OrderEvent event){
        log.info("inventory2 event take qwe = " + event);
        return Mono.fromRunnable(() -> this.service.cancelOrderInventory(event));
    }

}


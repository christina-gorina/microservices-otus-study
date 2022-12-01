package com.christinagorina.logistics.config;

import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.logistics.service.WarehouseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@Slf4j
@AllArgsConstructor
public class CatalogEventConsumer {

    private final WarehouseService warehouseService;

    @Bean
    public Consumer<Message<CatalogEvent>> catalogReservedConsumer() {
        return catalogEvent -> {
            log.info("catalogEvent qwe = " + catalogEvent.getPayload());
            warehouseService.warehouseReserve(catalogEvent);

        };
    }

}
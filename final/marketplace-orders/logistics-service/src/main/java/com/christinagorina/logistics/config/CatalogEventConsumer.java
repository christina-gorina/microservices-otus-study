package com.christinagorina.logistics.config;

import com.christinagorina.events.catalog.CatalogEvent;
import com.christinagorina.logistics.Service.WarehouseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.util.Objects;
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
//TODO сделать сообщения идемпотентными
            warehouseService.warehouseReserve(catalogEvent.getPayload());

            //TODO убрать в транзакцию
            Acknowledgment acknowledgment = catalogEvent.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
            if (Objects.nonNull(acknowledgment)) {
                log.info("acknowledgment != null qwe");
                acknowledgment.acknowledge();
                log.info("acknowledgment.acknowledge qwe");
            }
        };
    }

}
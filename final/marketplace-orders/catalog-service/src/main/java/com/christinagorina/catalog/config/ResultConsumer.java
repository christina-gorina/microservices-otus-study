package com.christinagorina.catalog.config;

import com.christinagorina.catalog.service.CatalogService;
import com.christinagorina.events.BillingEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@Slf4j
@AllArgsConstructor
public class ResultConsumer {

    private final CatalogService catalogService;

    @Bean
    public Consumer<Message<BillingEvent>> orderCheckResultConsumer() {
        return billingEvent -> {
            log.info("billingConsumer = " + billingEvent.getPayload());
            catalogService.saveResult(billingEvent.getPayload());
        };
    }

}
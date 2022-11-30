package com.christinagorina.catalog.config;

import com.christinagorina.catalog.service.CatalogService;
import com.christinagorina.events.order.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@Slf4j
@AllArgsConstructor
public class OrderConsumer {

    private final CatalogService catalogService;

    @Bean
    public Consumer<Message<OrderEvent>> catalogProcessor() {
        return orderEvent -> {
            log.info("orderEvent qwe = " + orderEvent.getPayload());
            //TODO проверить куда должно улететь событие
            catalogService.productItemReservation(orderEvent);
        };
    }

}